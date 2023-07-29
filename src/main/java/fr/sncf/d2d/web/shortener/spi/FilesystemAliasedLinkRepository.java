package fr.sncf.d2d.web.shortener.spi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkCreation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class FilesystemAliasedLinkRepository extends InMemoryAliasedLinkRepository {
    private final String fileName;
    private final ObjectMapper objectMapper;

    private final Logger logger = Logger.getLogger(FilesystemAliasedLinkRepository.class.getName());

    public FilesystemAliasedLinkRepository(
            Clock clock,
            @Value("${url-shortener.save.file.name}")
            String fileName,
            ObjectMapper objectMapper
    ) {
        super(clock);
        this.fileName = fileName;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        File file = new File(this.fileName);
        if (!file.exists()) {
            return;
        }
        try {
            AliasedLinkEntity[] list = this.objectMapper.readValue(file, AliasedLinkEntity[].class);
            Arrays.stream(list).forEach(this::add);
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Log file could not be loaded.");
        }
    }

    @Override
    public AliasedLink save(AliasedLinkCreation creation) {
        AliasedLink result = super.save(creation);
        this.flush();
        return result;
    }

    @Override
    public void remove(AliasedLink aliasedLink) {
        super.remove(aliasedLink);
        this.flush();
    }

    @Override
    public int removeOlderThan(LocalDateTime localDateTime) {
        int size = super.removeOlderThan(localDateTime);
        if (size != 0) {
            this.flush();
        }
        return size;
    }

    private synchronized void flush() {
        File file = new File(this.fileName);
        try {
            this.objectMapper.writeValue(file, this.data.values());
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Link entries could not be saved.");
        }
    }
}
