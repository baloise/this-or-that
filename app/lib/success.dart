import 'package:flutter/material.dart';

class SuccessScreen extends StatefulWidget {
  SuccessScreen({Key key, @required this.surveyCode, @required this.title})
      : super(key: key);

  final String title;
  final String surveyCode;

  @override
  SuccessScreenState createState() =>
      SuccessScreenState(surveyCode: surveyCode, title: title);
}

class SuccessScreenState extends State<SuccessScreen> {
  final String surveyCode;
  final String title;

  SuccessScreenState({Key key, @required this.surveyCode, @required this.title})
      : super();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Survey created"),
        ),
        body: SafeArea(
          child: Padding(
            child: Column(
              children: <Widget>[
                Text(title),
                Text(surveyCode),
              ],
            ),
            padding: const EdgeInsets.all(20),
          ),
        ));
  }
}
