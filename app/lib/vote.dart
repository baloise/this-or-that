import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'common/or_divider.dart';
import 'common/or_divider_vertical.dart';

const double OR_DIVIDER_PADDING = 30.0;

class VoteScreen extends StatefulWidget {
  VoteScreen({Key key, @required this.surveyCode}) : super(key: key);

  final String surveyCode;

  @override
  VoteScreenState createState() => VoteScreenState(surveyCode: surveyCode);
}

class VoteScreenState extends State<VoteScreen> {
  final String surveyCode;

  bool loading = false;
  String firstElement;
  String secondElement;

  VoteScreenState({Key key, @required this.surveyCode}) : super();

  @override
  void initState() {
    this.loadNewDecisionSet();
  }

  void loadNewDecisionSet() {
    this.loading = true;
    http.get("http://api.holzenkamp.me/spring-test/slow").then((response) {
      setState(() {
        this.loading = false;
        this.firstElement = "aa";
        this.secondElement = "bbb";
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Survey"),
        ),
        body: SafeArea(child: new OrientationBuilder(
          builder: (context, orientation) {
            if (orientation == Orientation.portrait) {
              return new Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Visibility(
                      visible: loading,
                      child: Expanded(
                          child: Center(child: CircularProgressIndicator()))),
                  Visibility(
                    visible: !loading,
                    child: VoteImageWidget(
                      imageId: firstElement,
                      winnerCallback: () {
                        voteFor(firstElement);
                      },
                    ),
                  ),
                  Visibility(
                    visible: !loading,
                    child: Padding(
                      padding: const EdgeInsets.all(OR_DIVIDER_PADDING),
                      child: OrDividerWidget(),
                    ),
                  ),
                  Visibility(
                    visible: !loading,
                    child: VoteImageWidget(
                      imageId: secondElement,
                      winnerCallback: () {
                        voteFor(secondElement);
                      },
                    ),
                  ),
                ],
              );
            }
            return new Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Visibility(
                    visible: loading,
                    child: Expanded(
                        child: Center(child: CircularProgressIndicator()))),
                Visibility(
                  visible: !loading,
                  child: VoteImageWidget(
                    imageId: firstElement,
                    winnerCallback: () {
                      voteFor(firstElement);
                    },
                  ),
                ),
                Visibility(
                  visible: !loading,
                  child: Padding(
                    padding: const EdgeInsets.all(OR_DIVIDER_PADDING),
                    child: OrDividerVerticalWidget(),
                  )
                ),
                Visibility(
                  visible: !loading,
                  child: VoteImageWidget(
                    imageId: secondElement,
                    winnerCallback: () {
                      voteFor(secondElement);
                    },
                  ),
                ),
              ],
            );
          },
        )));
  }

  voteFor(String winnerId) {
    print("aaa:" + winnerId);
  }
}

class VoteImageWidget extends StatelessWidget {
  final String imageId;
  final VoidCallback winnerCallback;

  VoteImageWidget({this.imageId, this.winnerCallback});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: GestureDetector(
          onTap: winnerCallback,
          child: new ClipRRect(
            borderRadius: new BorderRadius.circular(8.0),
            child: Image.network(
              "https://wallpaperplay.com/walls/full/4/9/f/196126.jpg",
              fit: BoxFit.fitWidth,
              loadingBuilder: (BuildContext context, Widget child,
                  ImageChunkEvent loadingProgress) {
                if (loadingProgress == null) return child;
                return Center(
                  child: CircularProgressIndicator(
                    value: loadingProgress.expectedTotalBytes != null
                        ? loadingProgress.cumulativeBytesLoaded /
                            loadingProgress.expectedTotalBytes
                        : null,
                  ),
                );
              },
            ),
          ),
        ),
      ),
    );
  }
}
