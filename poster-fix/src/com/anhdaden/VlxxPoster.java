package com.anhdaden;

import java.net.URI;
import org.jsoup.nodes.Element;

/** Accept both lazy-loaded catalog images and ordinary image URLs. */
public final class VlxxPoster {
    private VlxxPoster() { }

    public static String image(Element img, String base) {
        if (img == null) return null;
        for (String attribute : new String[] {"data-original", "data-src", "src"}) {
            String raw = img.attr(attribute).trim();
            if (raw.isEmpty()) continue;
            try {
                URI url = new URI(base + (base.endsWith("/") ? "" : "/")).resolve(raw);
                String scheme = url.getScheme();
                if (("https".equalsIgnoreCase(scheme) || "http".equalsIgnoreCase(scheme))
                        && url.getHost() != null && url.getUserInfo() == null) {
                    return url.toASCIIString();
                }
            } catch (IllegalArgumentException | java.net.URISyntaxException ignored) {
                // Ignore malformed attributes and try the next candidate.
            }
        }
        return null;
    }
}
