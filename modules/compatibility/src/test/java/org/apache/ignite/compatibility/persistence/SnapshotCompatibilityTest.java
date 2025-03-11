/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.compatibility.persistence;

import java.util.List;
import javax.cache.Cache;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cache.CacheEntry;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.compatibility.testframework.junits.IgniteCompatibilityAbstractTest;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.IgniteEx;
import org.apache.ignite.internal.util.typedef.internal.U;
import org.apache.ignite.lang.IgniteInClosure;
import org.junit.Test;

/**
 *
 */
public class SnapshotCompatibilityTest extends IgniteCompatibilityAbstractTest {
    /** */
    private static final String OLD_IGNITE_VERSION = "2.16.0";

    /** */
    private static final String SNAPSHOT_NAME = "test_snapshot";

    /**
     *
     */
    @Test
    public void testSnapshotRestore() throws Exception {
        try {
            startGrid(1, OLD_IGNITE_VERSION, new ConfigurationClosure(), new PostStartupClosure());

            stopAllGrids();

            cleanPersistenceDir(true);

            IgniteEx curIdn = startGrid(0);

            curIdn.snapshot().restoreSnapshot(SNAPSHOT_NAME, List.of("organizations")).get();

            IgniteCache<String, Integer> cache = curIdn.cache("organizations");

            for (int i = 0; i < 100_000; i++) {
                assertTrue(cache.containsKey("organization-" + i));
            }

            System.out.println("Cache read end");
        }
        finally {
            stopAllGrids();

            cleanPersistenceDir();
        }
    }

    /**
     *
     */
    private static class ConfigurationClosure implements IgniteInClosure<IgniteConfiguration> {
        /** {@inheritDoc} */
        @Override public void apply(IgniteConfiguration igniteConfiguration) {
            DataStorageConfiguration storageCfg = new DataStorageConfiguration();

            storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);

            igniteConfiguration.setDataStorageConfiguration(storageCfg);

            igniteConfiguration.setConsistentId(null);

            System.out.println("ConfigurationClosure.apply");
        }
    }

    /**
     *
     */
    private static class PostStartupClosure implements IgniteInClosure<Ignite> {
        /** {@inheritDoc} */
        @Override public void apply(Ignite ignite) {
            ignite.cluster().state(ClusterState.ACTIVE);

            IgniteCache<String, Integer> organizations = ignite.createCache("organizations");
            for (int i = 0; i < 100_000; i++)
                organizations.put("organization-" + i, i);

            ignite.snapshot().createSnapshot(SNAPSHOT_NAME).get();
            System.out.println("Snapshot has taken");
        }
    }
}