class DecisionSet {

  String id1;
  String id2;
  bool surveyIsRunning;
  String perspective;

  DecisionSet({this.id1, this.id2, this.surveyIsRunning, this.perspective});

  factory DecisionSet.fromJson(Map<String, dynamic> json) {
    return DecisionSet(
      id1: json['id1'],
      id2: json['id2'],
      surveyIsRunning: json['surveyIsRunning'],
      perspective: json['perspective'],
    );
  }

}

class DecisionChoice {

  String winner;
  String loser;

  DecisionChoice({this.winner, this.loser});

  Map toMap() {
    var map = new Map<String, dynamic>();
    map["winner"] = winner;
    map["loser"] = loser;

    return map;
  }

}

class ScoreSummary {

  int numberOfUsers;
  int numberOfVotes;
  String perspective;
  bool surveyIsRunning;
  List<ItemScore> scores;

  ScoreSummary({this.numberOfUsers, this.numberOfVotes, this.perspective, this.surveyIsRunning, this.scores});

  factory ScoreSummary.fromJson(Map<String, dynamic> json) {
    var list = json['scores'] as List;

    return ScoreSummary(
      numberOfUsers: json['numberOfUsers'],
      numberOfVotes: json['numberOfVotes'],
      perspective: json['perspective'],
      surveyIsRunning: json['surveyIsRunning'],
      scores: list.map((score) => ItemScore.fromJson(score)).toList(),
    );
  }

}

class ItemScore {

  String imageId;
  String file;
  int score;

  ItemScore({this.imageId, this.file, this.score});

  factory ItemScore.fromJson(Map<String, dynamic> json) {
    return ItemScore(
      imageId: json['imageId'],
      file: json['file'],
      score: json['score'],
    );
  }

}