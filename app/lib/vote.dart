import 'package:flutter/material.dart';

class VoteScreen extends StatelessWidget {

  final String surveyCode;

  VoteScreen({Key key, @required this.surveyCode}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Second Screen"),
      ),
      body: Center(
        child: RaisedButton(
          onPressed: () {
            Navigator.pop(context);
          },
          child: Text('Go back: ' + surveyCode),
        ),
      ),
    );
  }
}
