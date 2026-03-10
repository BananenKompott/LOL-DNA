package com.example.loldna.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RuneLookupService {

    private static final Logger log = LoggerFactory.getLogger(RuneLookupService.class);

    // Fixed patch for now; can be externalised to config if needed.
    private static final String RUNES_URL =
            "https://ddragon.leagueoflegends.com/cdn/14.2.1/data/en_US/runesReforged.json";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<Integer, RuneInfo> cache = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public List<RuneInfo> resolveRunes(List<Integer> runeIds) {
        if (runeIds == null || runeIds.isEmpty()) {
            return Collections.emptyList();
        }

        ensureLoaded();

        List<RuneInfo> result = new ArrayList<>();
        for (Integer id : runeIds) {
            if (id == null) continue;
            RuneInfo info = cache.get(id);
            if (info != null) {
                result.add(info);
            } else {
                log.debug("Rune id {} not found in Data Dragon cache", id);
            }
        }
        return result;
    }

    private synchronized void ensureLoaded() {
        if (initialized) {
            return;
        }
        try {
            String json = restTemplate.getForObject(RUNES_URL, String.class);
            if (json == null || json.isBlank()) {
                log.warn("Empty response from Data Dragon rune endpoint");
                return;
            }
            JsonNode root = objectMapper.readTree(json);
            if (!root.isArray()) {
                log.warn("Unexpected runesReforged.json structure");
                return;
            }

            for (JsonNode styleNode : root) {
                JsonNode slots = styleNode.get("slots");
                if (slots == null || !slots.isArray()) continue;

                for (JsonNode slot : slots) {
                    JsonNode runes = slot.get("runes");
                    if (runes == null || !runes.isArray()) continue;

                    for (JsonNode runeNode : runes) {
                        JsonNode idNode = runeNode.get("id");
                        JsonNode nameNode = runeNode.get("name");
                        JsonNode iconNode = runeNode.get("icon");

                        if (idNode == null || !idNode.isInt()) continue;

                        int id = idNode.asInt();
                        String name = nameNode != null ? nameNode.asText() : null;
                        String icon = iconNode != null ? iconNode.asText() : null;

                        cache.put(id, new RuneInfo(id, name, icon));
                    }
                }
            }

            initialized = true;
            log.info("Loaded {} runes into Data Dragon cache", cache.size());
        } catch (Exception ex) {
            log.warn("Failed to load runes from Data Dragon: {}", ex.toString());
        }
    }

    public static class RuneInfo {
        private final Integer id;
        private final String name;
        private final String icon; // Data Dragon relative path, e.g. "perk-images/..."

        public RuneInfo(Integer id, String name, String icon) {
            this.id = id;
            this.name = name;
            this.icon = icon;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }
    }
}

