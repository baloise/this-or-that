import 'package:flutter/material.dart';

import 'service/api-service.dart';
import 'service/dtos.dart';

class ResultScreen extends StatefulWidget {
  ResultScreen({Key key, @required this.surveyCode}) : super(key: key);

  final String surveyCode;

  @override
  ResultScreenState createState() => ResultScreenState(surveyCode: surveyCode);
}

class ResultScreenState extends State<ResultScreen> {
  final String surveyCode;

  Future<ScoreSummary> scoreSummaryFuture;

  ResultScreenState({Key key, @required this.surveyCode}) : super();

  String buildImageUrl(String imageId) {
    return ApiService.buildImageUrl(surveyCode, imageId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.blueAccent[700],
          title: Text("Results (Code: " + this.surveyCode + ")"),
        ),
        body: SafeArea(
            child: FutureBuilder<ScoreSummary>(
                future: ApiService.fetchScoreData(this.surveyCode),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.done &&
                      snapshot.hasData) {
                    return Padding(
                      padding: const EdgeInsets.all(20.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: <Widget>[
                          Text("Topic: " + snapshot.data.perspective,
                              style: TextStyle(
                                  fontSize: 20, fontWeight: FontWeight.bold)),
                          Text(
                              "Number of users: " +
                                  snapshot.data.numberOfUsers.toString(),
                              style: TextStyle(fontSize: 20)),
                          Text(
                              "Number of votes: " +
                                  snapshot.data.numberOfVotes.toString(),
                              style: TextStyle(fontSize: 20)),
                          Padding(
                            padding: const EdgeInsets.fromLTRB(0, 20, 0, 0),
                            child: Text("Results:",
                                style: TextStyle(
                                    fontSize: 20, fontWeight: FontWeight.bold)),
                          ),
                          Flexible(
                            child: ListView.builder(
                              itemCount: snapshot.data.scores.length,
                              itemBuilder: (context, index) {
                                return Card(
                                  child: Column(
                                    children: <Widget>[
                                      Image.network(buildImageUrl(
                                          snapshot.data.scores[index].imageId)),
                                      Text("Score: " +
                                          snapshot.data.scores[index].score
                                              .toString())
                                    ],
                                  ),
                                );
                              },
                            ),
                          )
                        ],
                      ),
                    );
                  }

                  return Center(child: CircularProgressIndicator());
                })));
  }
}
