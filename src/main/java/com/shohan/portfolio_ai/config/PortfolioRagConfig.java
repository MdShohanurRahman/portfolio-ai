package com.shohan.portfolio_ai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.util.List;

@Configuration
@Slf4j
public class PortfolioRagConfig {

    @Value("portfolio-store.json")
    private String vectorStoreName;

    @Value("classpath:/data/portfolio.json")
    private Resource models;

    private final ResourceLoader resourceLoader;

    public PortfolioRagConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean(name = "portfolioVectorStore")
    public SimpleVectorStore portfolioVectorStore(OpenAiEmbeddingModel embeddingModel) {
        var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        Resource resource = resourceLoader.getResource("classpath:/data/" + vectorStoreName);
        if (resource.exists()) {
            log.info("Vector Store File Exists in classpath, loading from classpath");
            simpleVectorStore.load(resource);
        } else {
            log.info("Vector Store File Does Not Exist, loading documents");
            var vectorStoreFile = getVectorStoreFile();
            TextReader textReader = new TextReader(models);
            textReader.getCustomMetadata().put("filename", "portfolio.txt");
            List<Document> documents = textReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        String absolutePath = "src/main/resources/data/" + vectorStoreName;
        return new File(absolutePath);
    }
}
