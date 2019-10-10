import 'package:flutter/material.dart';

import 'create/create.dart';
import 'results.dart';
import 'start.dart';
import 'vote.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'This or That',
      initialRoute: '/',
      routes: {
        '/': (context) => StartScreen(),
        '/create': (context) => CreateScreen(),
        '/vote': (context) => VoteScreen(
              surveyCode: null,
            ),
        '/results': (context) => ResultScreen(
              surveyCode: null,
            ),
      },
      theme: ThemeData(
        primaryColor: Colors.blueAccent[700],
        accentColor: Colors.tealAccent[400],
      ),
    );
  }
}
