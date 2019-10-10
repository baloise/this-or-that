# app

## building / releasing

### iOS

Currently the building profile is maintained by @MarkusTiede.

```
cd app && flutter clean && flutter pub get && flutter build ios
```

Afterwards continue building and publishing in Xcode.

### Android

Currently the building profile is maintained by @hirsch88.

```
cd app && flutter clean && flutter pub get && flutter build appbundle --build-name=Maj.Min.Patch+BuildNo
```

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://flutter.dev/docs/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://flutter.dev/docs/cookbook)

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.
