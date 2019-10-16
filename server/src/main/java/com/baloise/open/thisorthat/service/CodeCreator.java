/*
 * Copyright 2018 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baloise.open.thisorthat.service;

import org.hashids.Hashids;

class CodeCreator {

    static String createSurveyCode(long surveyIndex) {
        String stringAlphabet = "abcdefghijklmnopqrstuvwxyz1234567890";
        int minHashLength = 4;
        String salt = "thisorthat_survey";
        Hashids hashids = new Hashids(salt, minHashLength, stringAlphabet);
        return hashids.encode(surveyIndex);
    }

    static String createImageCode(long imageIndex) {
        String stringAlphabet = "abcdefghijklmnopqrstuvwxyz1234567890";
        int minHashLength = 1;
        String salt = "thisorthat_images";
        Hashids hashids = new Hashids(salt, minHashLength, stringAlphabet);
        return hashids.encode(imageIndex);
    }
}
