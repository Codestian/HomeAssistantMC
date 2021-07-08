![Logo of HomeAssistantMC](https://i.imgur.com/9SMxBA5.png)

HomeAssistantMC is a Minecraft mod that integrates Home Assistant to Minecraft, allowing retrieval of entity states and calling of services. This allows you to build and execute automations entirely ingame.

## Documentation
Setup, configure and learn how to use HomeAssistantMC on the [documentation site](https://codestian.github.io/HomeAssistantMC/).

## Project Setup

To build HomeAssistantMC from source code, clone the repository and open a Java IDE (IntelliJ recommended). Generate the run configurations with either `gradlew genEclipseRuns` (Eclipse) or `gradlew genIntellijRuns` (IntelliJ). Run `gradlew build` to generate the .jar file.

For the documentation site, run `npm install` followed by `npm run dev`. The site is built with Vuepress written in markdown format.