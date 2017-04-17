package com.eccentricyan.mypin.common.migration;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by shiyanhui on 2017/04/17.
 */

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        // For now anyone migration is needed!
    }

    // https://github.com/realm/realm-java/issues/1919#issuecomment-165375852
    @Override
    public boolean equals(Object o) {
        return o instanceof Migration;
    }
}