package com.anhdaden;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public final class VlxxPosterTest {
    private static int checks;
    private static final String BASE = "https://example.test";

    private static void verify(String expected, String html) {
        Element img = html == null ? null : Jsoup.parse(html).selectFirst("img");
        String result = VlxxPoster.image(img, BASE);
        if (expected == null ? result != null : !expected.equals(result)) {
            throw new AssertionError("Poster URL mismatch in synthetic case " + checks);
        }
        checks++;
    }

    public static void main(String[] args) throws Exception {
        verify("https://cdn.example.test/a.jpg", "<img src='data:image/gif;base64,AA==' data-original='https://cdn.example.test/a.jpg'>");
        verify("https://cdn.example.test/b.jpg", "<img src='https://cdn.example.test/b.jpg'>");
        verify("https://cdn.example.test/c.jpg", "<img data-src='//cdn.example.test/c.jpg' src='data:image/gif;base64,AA=='>");
        verify("https://example.test/img/d.jpg", "<img data-original='/img/d.jpg'>");
        verify("https://example.test/img/e.jpg", "<img data-original='data:image/gif;base64,AA==' src='/img/e.jpg'>");
        verify("https://example.test/img/f.jpg", "<img data-original='https://bad host/x.jpg' data-src='/img/f.jpg'>");
        verify(null, "<img src='data:image/gif;base64,AA=='>");
        verify(null, "<img src='javascript:alert(1)'>");
        verify(null, null);
        verify(null, "<img>");
        verify("https://example.test/g.jpg", "<img data-original='   ' src=' /g.jpg '>");
        verify("https://cdn.example.test/h.jpg", "<img data-original='https://cdn.example.test/h.jpg' src='/placeholder.gif'>");
        verify("https://example.test/j.jpg", "<img data-original='https://user:secret@cdn.example.test/private.jpg' src='/j.jpg'>");
        System.out.println("Synthetic poster tests passed: " + checks);
        for (String path : args) {
            String html = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            int count = 0;
            for (Element img : Jsoup.parse(html).select("#video-list .video-item img.video-image")) {
                String url = VlxxPoster.image(img, BASE);
                if (url == null || !url.startsWith("http")) throw new AssertionError("Missing live catalog poster");
                count++;
            }
            if (count < 10) throw new AssertionError("Insufficient live cards");
            System.out.println("Live catalog poster URLs valid: " + count);
        }
    }
}
