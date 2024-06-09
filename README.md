# elimu.ai Content Provider

Android application which downloads educational content (e.g. letters, words, audios, storybooks, videos, etc) to the 
device and provides it to other elimu.ai apps.

![](https://user-images.githubusercontent.com/15718174/76617075-6c82d200-6560-11ea-867d-e46385017e03.png)

See software architecture diagram at https://github.com/elimu-ai/model/blob/main/README.md

## Software Architecture

[
  <img width="320" alt="Software Architecture" src="https://user-images.githubusercontent.com/15718174/83595568-fb6a1e00-a594-11ea-990a-10c0bd62ed11.png">
](https://github.com/elimu-ai/wiki/blob/main/SOFTWARE_ARCHITECTURE.md)

## Utils Library 📦

The Content Provider comes with a [`utils`](utils) library (`.aar`) which makes it easier for other Android apps to fetch content from the Content Provider.

Here is how to use the `utils` library in another Android app:

  1. Add repository:
  
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
  
  2. Add dependency:
  
    dependencies {
        implementation 'com.github.elimu-ai:content-provider:<version>@aar'
    }

See https://jitpack.io/#elimu-ai/content-provider/ for the latest version available.

### Usage Example

For an example of another Android app using the `utils` library, see the Vitabu app's Gradle configuration:

  1. https://github.com/elimu-ai/vitabu/blob/main/build.gradle
  1. https://github.com/elimu-ai/vitabu/blob/main/app/build.gradle

## Development 👩🏽‍💻

During development, you can choose between 3 build types:
1. `debug`
2. `qa_test`
3. `release`

By default, both `debug` and `qa_test` are pointing to a webapp 
[test server](https://github.com/elimu-ai/webapp/blob/main/INSTALL.md#test-server). And if you want 
to  run the webapp yourself on `localhost` while testing the Content Provider app, you can change 
the return value of the `getBaseUrl` method in 
[BaseApplication.java](app/src/main/java/ai/elimu/content_provider/BaseApplication.java):
```java
//        return url;
        return "http://192.168.xxx.xxx:8080/webapp";
```

Use the `ifconfig` or `ipconfig` to get your IPv4 address.

You will also have to enable http connections by adding the following file to 
`app/src/main/res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

And then adding it to [AndroidManifest.xml](app/src/main/AndroidManifest.xml):
```xml
<application
        android:networkSecurityConfig="@xml/network_security_config"
```

After that, connect your Android device to the same Wi-Fi network as your computer, and run the app.

### Gradle Upgrade

```
./gradlew wrapper --gradle-version x.x.x --distribution-type all
```

### Database Migration 🔀

When adding a new database `@Entity` (or modifying an existing one), you need to prepare a database
migration (SQL script) in
[`app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java`](app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java).

Follow these steps:

1. Add the new/modified `@Entity` to [`app/src/main/java/ai/elimu/content_provider/room/entity/`](app/src/main/java/ai/elimu/content_provider/room/entity/)
1. Add the entity's DAO interface to [`app/src/main/java/ai/elimu/content_provider/room/dao/`](app/src/main/java/ai/elimu/content_provider/room/dao/)
1. Include the DAO interface in [`app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java`](app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java)
1. Include the entity in the `entities` section of the `@Database` in [`app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java`](app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java)
1. Bump the `@Database` version in [`app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java`](app/src/main/java/ai/elimu/content_provider/room/db/RoomDb.java)
1. Build the code with `./gradlew clean build`
1. Open the new database schema generated at `app/schemas/ai.elimu.content_provider.room.db.RoomDb/<version>.json`
- Under `entities`, find the matching `tableName` and copy its SQL script from the `createSql` property.
1. Open `RoomDb.java` and add a new method for the latest migration
- Paste the SQL script from the above JSON schema, and replace `${TABLE_NAME}` with the name of the table you created/modified.
- Include the migration in the `getDatabase` method in `RoomDb.java`.
1. To run the database migration, launch the application on your device.

**Tip #1:** To verify that your database migration ran successfully, look at the Logcat output and
ensure that there are no RoomDb errors:
```
2023-11-27 11:46:50.662  6124-13233 ai.elimu.c....RoomDb$18 ai.elimu.content_provider.debug      I  migrate (23 --> 24)
2023-11-27 11:46:50.663  6124-13233 ai.elimu.c....RoomDb$18 ai.elimu.content_provider.debug      I  sql: CREATE TABLE IF NOT EXISTS `LetterSound` (`revisionNumber` INTEGER NOT NULL, `usageCount` INTEGER, `id` INTEGER, PRIMARY KEY(`id`))
```

**Tip #2:** You can also use Android Studio's _Database Inspector_ to verify that the database
migration succeeded:

![](https://github.com/elimu-ai/content-provider/assets/1451036/4c462813-bac0-4d4c-9f62-8c4aa12252d9)

## Release 📦

To perform a release, follow these steps:

1. Create a new branch for the release
1. Remove `-SNAPSHOT`
  - from the `versionName` in `app/build.gradle`
  - from the `versionName` in `utils/build.gradle`
  - from the `version` in `utils/build.gradle` under `publishing`
1. Commit the changes
1. Click "Draft a new release" at https://github.com/elimu-ai/content-provider/releases
1. Pick the new branch you created as the target branch
1. Create a new tag (e.g. `1.2.19`)
1. Choose a release title, and click "Publish release"
1. Ensure that the release appears at https://jitpack.io/#elimu-ai/content-provider with "Status: ok"
1. Prepare for next development iteration by bumping the version and adding `-SNAPSHOT`
  - in the `versionCode` in `app/build.gradle`
  - in the `versionName` in `app/build.gradle`
  - in the `versionCode` in `utils/build.gradle`
  - in the `versionName` in `utils/build.gradle`
  - in the `version` in `utils/build.gradle` under `publishing`
1. Commit the changes
   1. Create a pull request for merging your branch into `main`

---

<p align="center">
  <img src="https://github.com/elimu-ai/webapp/blob/main/src/main/webapp/static/img/logo-text-256x78.png" />
</p>
<p align="center">
  elimu.ai - Free open-source learning software for out-of-school children ✨🚀
</p>
<p align="center">
  <a href="https://elimu.ai">Website 🌐</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/elimu-ai/wiki#readme">Wiki 📃</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/orgs/elimu-ai/projects?query=is%3Aopen">Projects 👩🏽‍💻</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/elimu-ai/wiki/milestones">Milestones 🎯</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/elimu-ai/wiki#open-source-community">Community 👋🏽</a>
  &nbsp;•&nbsp;
  <a href="https://www.drips.network/app/drip-lists/41305178594442616889778610143373288091511468151140966646158126636698">Support 💜</a>
</p>
