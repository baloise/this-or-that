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
package com.baloise.open.thisorthat.vote.image.selection;

import com.baloise.open.thisorthat.db.InMemoryDatabase;
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RandomSelectionAlgorithm implements ImageSelectionAlgorithm {

    private final InMemoryDatabase inMemoryDatabase;

    private final Random rand = new Random();

    public RandomSelectionAlgorithm(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    @Override
    public Pair<Image> getNextImagePair(String surveyCode, String userId) {
        List<String> ids = getImageIdsNotYetVotedFor(surveyCode, userId);
        if (ids.size() > 0) {
            return this.getNextImages(surveyCode, ids);
        } else {
            return this.getRandomImages(surveyCode);
        }
    }

    private Pair<Image> getNextImages(String surveyCode, List<String> imageIds) {
        Set<String> selection = new HashSet<>();
        if (imageIds.size() >= 2) {
            while (selection.size() < 2) {
                selection.add(imageIds.get(rand.nextInt(imageIds.size())));
            }
        } else {
            selection.add(imageIds.get(0));
            while (selection.size() < 2) {
                selection.add(getRandomImageFromList(surveyCode).getId());
            }
        }
        List<String> selectionList = new ArrayList<>(selection);
        Collections.shuffle(selectionList);
        return new Pair<>(inMemoryDatabase.getImageFromSurvey(surveyCode, selectionList.get(0)),
                inMemoryDatabase.getImageFromSurvey(surveyCode, selectionList.get(1)));
    }

    private Pair<Image> getRandomImages(String surveyCode) {
        Set<Image> selection = new HashSet<>();
        while (selection.size() < 2) {
            selection.add(getRandomImageFromList(surveyCode));
        }
        List<Image> selectionList = new ArrayList<>(selection);
        Collections.shuffle(selectionList);
        return new Pair<>(selectionList.get(0), selectionList.get(1));
    }

    private Image getRandomImageFromList(String surveyCode) {
        List<Image> images = inMemoryDatabase.getSurvey(surveyCode).getImages();
        return images.get(rand.nextInt(images.size()));
    }

    private List<String> getImageIdsNotYetVotedFor(String surveyCode, String userId) {
        List<String> imageIds = inMemoryDatabase.getSurvey(surveyCode).getImages().stream().map(Image::getId).collect(Collectors.toList());
        inMemoryDatabase.getSurvey(surveyCode).getVotes().stream()
                .filter(f -> f.getUserId().equals(userId))
                .forEach(f -> {
                    imageIds.remove(f.getLoser());
                    imageIds.remove(f.getWinner());
                });
        return imageIds;
    }

}
