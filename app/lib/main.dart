import 'package:flutter/material.dart';

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
        '/vote': (context) => VoteScreen(),
      },
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
