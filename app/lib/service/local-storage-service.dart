import 'package:shared_preferences/shared_preferences.dart';

import 'dtos.dart';

const STORAGE_KEY = "thisOrThatHistory";

class LocalStorageService {
  static void saveParticipated(String surveyId, String perspective) {
    SharedPreferences.getInstance().then((instance) {
      ParticipatedSurvey participatedSurvey = new ParticipatedSurvey(
          surveyId: surveyId,
          dateTime: DateTime.now(),
          perspective: perspective);

      List<String> store;

      try {
        store = instance.getStringList(STORAGE_KEY);
      } catch (Exception) {
        store = [];
      }

      if (store == null) {
        store = [participatedSurvey.serialize()];
      } else {
        bool isAlreadyContained = store
            .map((json) => ParticipatedSurvey.deserialize(json))
            .map((survey) => survey.surveyId)
            .contains(surveyId);

        if (!isAlreadyContained) {
          store.add(participatedSurvey.serialize());
        }
      }

      instance.setStringList(STORAGE_KEY, store);
      print("wrote " + store.toString() + " to store");
    });
  }

  static Future<List<String>> getParticipated() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getStringList(STORAGE_KEY);
  }
}
