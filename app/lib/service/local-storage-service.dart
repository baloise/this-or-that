import 'package:shared_preferences/shared_preferences.dart';

import 'dtos.dart';

const STORAGE_KEY = "thisOrThatHistory";
const ADMIN_KEY = "thisOrThatAdmin";

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

  static void saveAdmin(String surveyId) {
    SharedPreferences.getInstance().then((instance) {
      List<String> store;

      try {
        store = instance.getStringList(ADMIN_KEY);
      } catch (Exception) {
        store = [];
      }

      if (store == null) {
        store = [surveyId];
      } else {
        if (!store.contains(surveyId)) {
          store.add(surveyId);
        }
      }

      instance.setStringList(ADMIN_KEY, store);
      print("wrote " + store.toString() + " to store");
    });
  }

  static Future<List<String>> getParticipated() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getStringList(STORAGE_KEY);
  }

  static Future<bool> isAdminOfSurvey(String surveyId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
     var stringList = prefs.getStringList(ADMIN_KEY);
     return stringList.contains(surveyId);
  }
}
