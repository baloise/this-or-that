---?image=/img/bin/iconset/logo.iconset/icon_512x512.png&size=50%&position=center

+++


@snap[midpoint span-40]

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

@snap[midpoint span-60]

### Code Camp Scope
- new frontend without baloise branding
- app for PlayStore and AppleStore
- backend with Spring Boot
- improve Azure Cloud hosting

@snapend

---

@snap[midpoint span-60]

### Backend
<img src="https://upload.wikimedia.org/wikipedia/de/thumb/e/e1/Java-Logo.svg/127px-Java-Logo.svg.png" alt="java" width="100"/>
<img src="https://camo.githubusercontent.com/12136cf9daa20a57168a9bdee376f2e83e13c5b1/68747470733a2f2f7069636f636c692e696e666f2f696d616765732f737072696e672d626f6f742e706e67" alt="spring boot" width="200"/>
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a8/Microsoft_Azure_Logo.svg/320px-Microsoft_Azure_Logo.svg.png" alt="java" width="200"/>
<img src="https://miro.medium.com/max/269/1*PSmlTRCSmWVXuuNOWtx9DQ.png" alt="lombok" width="200"/>
<img src="https://webassets.mongodb.com/_com_assets/cms/MongoDB_Logo_FullColorBlack_RGB-4td3yuxzjs.png" alt="lombok" width="200"/>
<img src="https://www.ictshore.com/wp-content/uploads/2018/08/sfw0002-01-REST_Architecture.png" alt="rest" width="200"/>

@snapend

+++

@snap[west span-60 text-06]

### REST
* createSurvey 
    * POST
    * path: /create -> surveyCode
* addImageToSurvey 
    * POST
    * path: /{code}/image
* startSurvey 
    * POST
    * path: /{code}/start
* getVote 
    * GET
    * path: /{code}/vote

@snapend


@snap[east span-60 text-06]

* getImageFromSurvey 
    * GET
    * path: /{code}/image/{imageId}
* setVote 
    * POST
    * path: /{code}/vote
* stopSurvey
    * POST 
    * path: /{code}/stop
* getScore 
    * GET
    * path: /{code}/score

@snapend

+++

@snap[midpoint span-60]

### Algorithms
- ImageSelectionAlgorithm
    - selects images from image pool
- VoteAlgorithm
    - keeps track of votes and calculates score

@snapend

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

# Vue

+++

# part 2 details

---

# Demo

+++

# part 2 details

---

# LL

+++

# credits
