package com.xzgedu.supercv.article.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class MarkdownConverter {
    private Parser parser;
    private HtmlRenderer htmlRenderer;

    public MarkdownConverter() {
        this.parser = Parser.builder().build();
        this.htmlRenderer = HtmlRenderer.builder().escapeHtml(true).build();
    }

    public String toPlainText(String markdown) {
        Node document = parser.parse(markdown);
        String html = htmlRenderer.render(document);
        String plainText = Jsoup.parse(html).text();
        return plainText;
    }
}
