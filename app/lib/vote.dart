import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class VoteScreen extends StatefulWidget {
  VoteScreen({Key key, @required this.surveyCode}) : super(key: key);

  final String surveyCode;

  @override
  VoteScreenState createState() => VoteScreenState(surveyCode: surveyCode);
}

class VoteScreenState extends State<VoteScreen> {

  final String surveyCode;
  bool loading = false;

  VoteScreenState({Key key, @required this.surveyCode}) : super();

  @override
  void initState() {
    this.loading = true;
    http.get("http://api.holzenkamp.me/spring-test/slow").then((response) {
      setState(() => this.loading = false);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Survey"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Visibility(
                visible: loading,
                child: CircularProgressIndicator()
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text("Survey-ID: " + surveyCode),
            )
          ],
        ),
      ),
    );
  }
}
