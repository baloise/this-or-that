---?color=linear-gradient(100deg, white 50%, #039 50.2%)

@snap[west span-40]
![logo](/web/src/assets/logo.png)
@snapend

@snap[east span-40 h3-white h4-white]
### This or That
#### Code-Camp 2019
@snapend

+++

## Bet

- Wurde eingereicht als Bet
- Ziel schnell und einfach Entscheiden (instant vote)
- Wurde in 2 Tagen umgesetzt

+++

## Idee

<img src="/docs/slides/img/this_or_that_customer_jouney.png" alt="customer Journey" width="400"/>

+++

## Umfrage erstellen

![create survey](/docs/slides/img/this_or_that_create_survey.png)

+++

## Code - QR

<img src="/docs/slides/img/this_or_that_survey_code_qr.png" alt="code" width="600"/>

+++

## Abstimmen

![voting](/docs/slides/img/this_or_that_voting.png)

+++

## Resultat

<img src="/docs/slides/img/this_or_that_result.jpg" alt="result" width="600"/>

+++

## Transformation

![GitHub](https://i.pinimg.com/600x315/2c/b6/70/2cb670b6ddd8922a1c1b2fee4f6f758c.jpg)
https://github.com/baloise/this-or-that

+++

## Code Camp Scope

- Neues Frontend ohne Baloise Branding
- App für PlayStore und AppleStore
- Backend mit Spring Boot
- Betrieb mit Azure Cloud verbessern

---

## Backend

<img src="https://upload.wikimedia.org/wikipedia/de/thumb/e/e1/Java-Logo.svg/127px-Java-Logo.svg.png" alt="java" width="100"/>
<img src="https://camo.githubusercontent.com/12136cf9daa20a57168a9bdee376f2e83e13c5b1/68747470733a2f2f7069636f636c692e696e666f2f696d616765732f737072696e672d626f6f742e706e67" alt="spring boot" width="200"/>
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a8/Microsoft_Azure_Logo.svg/320px-Microsoft_Azure_Logo.svg.png" alt="java" width="200"/>
<img src="https://miro.medium.com/max/269/1*PSmlTRCSmWVXuuNOWtx9DQ.png" alt="lombok" width="200"/>
<img src="https://webassets.mongodb.com/_com_assets/cms/MongoDB_Logo_FullColorBlack_RGB-4td3yuxzjs.png" alt="lombok" width="200"/>
<img src="https://www.ictshore.com/wp-content/uploads/2018/08/sfw0002-01-REST_Architecture.png" alt="rest" width="200"/>

---

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

---

# App-Store

+++

# part 2 details

---

@snap[north]

## Web Client

@fa[quote-left text-gray](Only one week time for developing)

@snapend

- Simple to learn
- Easy to deploy (github pages)
- A good documentation & community
- Progressive Web App (PWA) Support

+++

![stats](/docs/slides/img/js-frameworks-stat.png)

+++?image=https://miro.medium.com/max/3920/1*Vc0m5dS9SlhieEbR6n8wFg.jpeg

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
---

# Demo

+++

# part 2 details

---

# LL

+++

# credits
