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

See https://jitpack.io/#elimu-ai/content-provider/

### Usage Example

For an example of another Android app using the `utils` library, see the Vitabu app's Gradle configuration:

  1. https://github.com/elimu-ai/vitabu/blob/master/build.gradle
  1. https://github.com/elimu-ai/vitabu/blob/master/app/build.gradle


---

elimu.ai - Free personalized learning for every child on Earth 🌍🌏🌎

[Website 🌐](https://elimu.ai) &nbsp; [Wiki 📃](https://github.com/elimu-ai/wiki#readme) &nbsp; [Projects 👩🏽‍💻](https://github.com/elimu-ai/wiki/projects) &nbsp; [Milestones 🎯](https://github.com/elimu-ai/wiki/milestones) &nbsp; [Community 👋🏽](https://github.com/elimu-ai/wiki#open-source-community)
