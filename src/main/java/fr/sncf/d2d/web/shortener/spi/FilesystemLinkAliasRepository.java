package fr.sncf.d2d.web.shortener.spi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sncf.d2d.web.shortener.domain.LinkAlias;
import fr.sncf.d2d.web.shortener.domain.LinkAliasCreation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Clock;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class FilesystemLinkAliasRepository extends InMemoryLinkAliasRepository {
    private final String fileName;
    private final ObjectMapper objectMapper;

    private final Logger logger = Logger.getLogger(FilesystemLinkAliasRepository.class.getName());

    /**
     * Thread pool of size one to insure synchronization.
     * Only one thread can be running.
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * Whether a write operation is already queued.
     * This flag allows for only one runnable to be awaiting execution.
     */
    private volatile boolean writeQueued = false;

    public FilesystemLinkAliasRepository(
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
            LinkAliasEntity[] array = this.objectMapper.readValue(file, LinkAliasEntity[].class);
            Arrays.stream(array).forEach(this::add);
        } catch (FileNotFoundException e) {
            this.logger.info("Log file not found, no data has been loaded.");
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Log file could not be loaded.", e);
        }
    }

    @Override
    public LinkAlias save(LinkAliasCreation creation) {
        LinkAlias result = super.save(creation);
        this.flush();
        return result;
    }

    @Override
    public void touch(UUID id) {
        super.touch(id);
        this.flush();
    }

    @Override
    public void remove(LinkAlias linkAlias) {
        super.remove(linkAlias);
        this.flush();
    }

    @Override
    public int removeOlderThan(TemporalAmount interval) {
        int size = super.removeOlderThan(interval);
        if (size != 0) {
            this.flush();
        }
        return size;
    }

    private synchronized void flush() {
        if (this.writeQueued) {
            return;
        }
        this.writeQueued = true;
        this.executor.submit(this::doFlush);
    }

    private void doFlush() {
        this.writeQueued = false;
        File file = new File(this.fileName);
        try {
            this.objectMapper.writeValue(file, this.data.values());
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Link entries could not be saved.", e);
        }
    }
}
