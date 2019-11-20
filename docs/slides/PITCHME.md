---?color=linear-gradient(100deg, white 50%, #1871e4 50.2%)

@snap[west span-40]
![logo](/web/src/assets/logo.png)
@snapend

@snap[east span-40 h3-white h4-white]
### This or That
#### Code-Camp 2019
@snapend

+++

@snap[midpoint span-80]

### Bet
- submitted as bet
- goal
    - tool for simple and instant voting
- was implemented in two days

@snapend

+++

@snap[midpoint span-50]

### Idea
![customer journey](/docs/slides/img/this_or_that_customer_jouney.png)
@snapend

+++

@snap[midpoint span-60]

### Create Survey
![create survey](/docs/slides/img/this_or_that_create_survey.png)

@snapend

+++

@snap[midpoint span-60]

### Code - QR
![customer journey](/docs/slides/img/this_or_that_survey_code_qr.png)

@snapend

+++

@snap[midpoint span-60]

### Voting
![voting](/docs/slides/img/this_or_that_voting.png)

@snapend

+++

@snap[midpoint span-50]

### Result
![result](/docs/slides/img/this_or_that_result.jpg)


@snapend

+++

@snap[midpoint span-60]

### Transformation
![GitHub](https://i.pinimg.com/600x315/2c/b6/70/2cb670b6ddd8922a1c1b2fee4f6f758c.jpg) 
https://github.com/baloise/this-or-that

@snapend

+++

@snap[midpoint span-80]

### Code Camp Scope
- new frontend without baloise branding
- app for PlayStore and AppleStore
- backend with Spring Boot
- improve Azure Cloud hosting

@snapend

---?color=#1871e4

@snap[east span-30]
![spring](https://spring.io/img/spring-by-pivotal.png)
@snapend

@snap[south-east span-30]
![lombok](https://miro.medium.com/max/269/1*PSmlTRCSmWVXuuNOWtx9DQ.png)
@snapend


@snap[north span-100]
# Backend
@snapend

@snap[midpoint span-30]
![rest](https://www.ictshore.com/wp-content/uploads/2018/08/sfw0002-01-REST_Architecture.png)
@snapend

@snap[south span-30]
![java](https://upload.wikimedia.org/wikipedia/de/thumb/e/e1/Java-Logo.svg/127px-Java-Logo.svg.png)
@snapend


@snap[west span-30]
![mongo](https://webassets.mongodb.com/_com_assets/cms/MongoDB_Logo_FullColorBlack_RGB-4td3yuxzjs.png)
@snapend

@snap[south-west span-30]
![azure](https://upload.wikimedia.org/wikipedia/commons/thumb/a/a8/Microsoft_Azure_Logo.svg/320px-Microsoft_Azure_Logo.svg.png)
@snapend

+++

@snap[midpoint span-60]

<img src="https://camo.githubusercontent.com/12136cf9daa20a57168a9bdee376f2e83e13c5b1/68747470733a2f2f7069636f636c692e696e666f2f696d616765732f737072696e672d626f6f742e706e67" alt="spring boot" width="200"/>
### Why Spring Boot?
- easy to set up
- easy to run on cloud
- less code
- less dependencies
- more standards

@snapend

+++

@snap[north span-60 text-center]

### REST

@snapend

@snap[west span-60 text-05 text-center]

1. createSurvey 
    * POST
    * path: /create -> surveyCode
2. addImageToSurvey 
    * POST
    * path: /{code}/image
3. startSurvey 
    * POST
    * path: /{code}/start
4. getVote 
    * GET
    * path: /{code}/vote

@snapend

@snap[east span-60 text-05 text-center]

5. getImageFromSurvey 
    * GET
    * path: /{code}/image/{imageId}
6. setVote 
    * POST
    * path: /{code}/vote
7. stopSurvey
    * POST 
    * path: /{code}/stop
8. getScore 
    * GET
    * path: /{code}/score

@snapend

+++

@snap[midpoint span-80]

### Algorithms
- ImageSelectionAlgorithm
    - selects images from image pool
- VoteAlgorithm
    - keeps track of votes and calculates score

@snapend

+++

@snap[midpoint span-80]

### Azure
- cloud solution from Microsoft
- app service based Tomcat 9
- deployment with ftp (only outside of baloise net)
- monitoring with the azure portal (only inside baloise net)

@snapend

---?color=#1871e4

# Flutter

+++

## About

- Open-Source UI Framework
- Google, 2017
- Language: Dart
- Flutter Engine

+++

## Flutter-Engine

- ~~Native App~~
- ~~Web-View~~
- **Low-Level-Rendering**
  - Google Skia Graphics Engine
  - Design: Material, Cupertino, ...
- Interaction w/ platform API (camera, push, ...)

+++

## Codecamp

- Scope: This-Or-That app for iOS & Android
- Features:
  - QR code scanning
  - Participate in existing surveys
  - Create a survey & upload pictures
  - Survey results
  - History of surveys

+++

## Experience

- Short time to market
- Good IDE support
- Libraries for Plattform API integration
- Comfortable development (Emulator & instant refresh)
- No framework issues
- Styling similar to Web, Layout is not

+++

# Demo

---?color=linear-gradient(100deg, white 50%, #1871e4 50.2%)

@snap[west span-40 text-center text-08]
![logo](https://seeklogo.net/wp-content/uploads/2018/04/apple-app-store-logo.png)

## Apple App Store

@snapend

@snap[east span-40 text-center h2-white text-08]
![logo](https://cnet3.cbsistatic.com/img/eBJSY1Bs6ZzDpzNQGURVqMblZec=/1092x0/2017/05/11/c4c06fe6-4534-416b-948d-7928acdd5c0a/googleplaystorelogo.png)

## Google Play Store

@snapend
---?color=#1871e4

@snap[h1-white]
# Web Client
@snapend

+++?color=linear-gradient(100deg, white 40%, #1871e4 40%)

@snap[west span-30 text-center text-08]
## Web Client
@snapend

@snap[east span-50 text-center h3-white text-white text-08]
### Requirement
- Simple to learn
- Easy to deploy (github pages)
- A good documentation & community
- Progressive Web App (PWA) Support
    - New Browser Features
@snapend

+++

![stats](/docs/slides/img/js-frameworks-stat.png)

+++?color=linear-gradient(100deg, white 40%, #40b984 40%)

@snap[west span-30 h2-gray text-center]

![logo](https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png)

## Vue.js

@snapend

@snap[east span-60 text-white text-08 text-center]

- Open-Source Framework
- Simple & Flexible
- Small learning curve
- Ecosystem (Router, Store...)
- Performance & Package Size
- Used by => Alibaba, GitLab, 9Gag

@snapend

+++

### Vue Component

@snap[text-08]
```html
<template>
  <h1>{{ message }}</h1>
</template>

<script lang="ts">
  import { Component, Vue, Prop } from "vue-property-decorator";

  @Component
  export default class HelloWorldComponent extends Vue {
    @Prop() message!: string;
  }
</script>

<style lang="scss" scoped>
  h1 {
    color: red;
  }
</style>
```
@snapend

+++

## PWA

### Progressive Web App

@fa[quote-left text-gray](PWA is the latest ‘buzzword’)

+++

## PWA

- Are written in HTML, CSS & JS
- Can be installed to the mobile home screen
- Runs in the browser but with access to the device features
- Can be used offline
- Can use web push notifications

+++

@snap[north span-100]
## PWA
### manifest.json
@snapend

@snap[text-08]
```json
{
  "name": "this-or-that-web",
  "short_name": "this-or-that-web",
  "icons": [
    {
      "src": "./img/icons/android-chrome-192x192.png",
      "sizes": "192x192", "type": "image/png"
    },
    {
      "src": "./img/icons/android-chrome-512x512.png",
      "sizes": "512x512", "type": "image/png"
    }
  ],
  "start_url": "./index.html",
  "display": "standalone",
  "background_color": "#FFFFFF",
  "theme_color": "#FFFFFF"
}
```
@snapend

+++

@snap[north span-100]
## PWA
### Browser Features
@snapend

@snap[text-08]
```typescript
@Ref('preview') public video!: HTMLVideoElement;

public async scan() {
    this.isScanEnabled = true;
    this.stream = await navigator.mediaDevices.getUserMedia({audio: false, video: {facingMode: 'environment'}});
    this.video.srcObject = this.stream;
    await this.video.play();
    requestAnimationFrame(this.tick);
}
```
@snapend

---?color=#1871e4

### Demo

![demo](https://media.giphy.com/media/U3shPfaqOAPFeucjIb/giphy.gif)

---?color=#1871e4

### Lesson Learned

- changing to Spring Boot was very easy
- Azure is not suited for Java Applications
