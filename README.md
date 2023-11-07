# elimu.ai Content Provider

Android application which downloads educational content (e.g. letters, words, audios, storybooks, videos, etc) to the 
device and provides it to other elimu.ai apps.

![](https://user-images.githubusercontent.com/15718174/76617075-6c82d200-6560-11ea-867d-e46385017e03.png)

See software architecture diagram at https://github.com/elimu-ai/model/blob/master/README.md

## Software Architecture

[
  <img width="320" alt="Software Architecture" src="https://user-images.githubusercontent.com/15718174/83595568-fb6a1e00-a594-11ea-990a-10c0bd62ed11.png">
](https://github.com/elimu-ai/wiki/blob/master/SOFTWARE_ARCHITECTURE.md)

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
`app/src/main/res/network_security_config.xml`:
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

## Release 📦

See [.github/workflows/gradle-release.yml](.github/workflows/gradle-release.yml)

---

elimu.ai - Free open source learning software for out-of-school children ✨🚀

[Website 🌐](https://elimu.ai) &nbsp; [Wiki 📃](https://github.com/elimu-ai/wiki#readme) &nbsp; [Projects 👩🏽‍💻](https://github.com/orgs/elimu-ai/projects?query=is%3Aopen) &nbsp; [Milestones 🎯](https://github.com/elimu-ai/wiki/milestones) &nbsp; [Community 👋🏽](https://github.com/elimu-ai/wiki#open-source-community)
