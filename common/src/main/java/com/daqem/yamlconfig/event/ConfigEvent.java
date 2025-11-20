package com.daqem.yamlconfig.event;

import com.daqem.yamlconfig.api.config.IConfig;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.level.Level;

public interface ConfigEvent {

    /**
     * @see Update#update(IConfig, Level)
     */
    Event<Update> ON_UPDATE = EventFactory.createLoop();

    interface Update {

        /**
         * Invoked after the configuration has been updated.
         * For client-side configurations, the update will occur on the client level.
         * For server-side and common configurations, the update will occur on the server level.
         *
         * @param config The updated configuration.
         * @param level  The level where the update occurred.
         */
        void update(IConfig config, Level level);
    }
}
