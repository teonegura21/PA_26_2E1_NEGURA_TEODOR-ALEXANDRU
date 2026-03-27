package lab5;

import lab5.model.Resource;
import java.util.*;

public class KeywordSetCover {

    public static Set<Resource> greedySetCover(Set<String> concepts, Collection<Resource> resources) {
        Set<String> uncovered = new HashSet<>(concepts);
        Set<Resource> selected = new HashSet<>();

        while (!uncovered.isEmpty()) {
            Resource best = null;
            int maxCover = 0;

            for (Resource r : resources) {
                Set<String> cover = new HashSet<>(r.getKeywords());
                cover.retainAll(uncovered);
                if (cover.size() > maxCover) {
                    maxCover = cover.size();
                    best = r;
                }
            }

            if (best == null || maxCover == 0) {
                break;
            }

            selected.add(best);
            uncovered.removeAll(best.getKeywords());
        }

        return selected;
    }

    public static Set<Resource> bruteForceMinimumSetCover(Set<String> concepts, Collection<Resource> resources) {
        List<Resource> resourceList = new ArrayList<>(resources);
        int n = resourceList.size();
        Set<Resource> bestSolution = null;
        int bestSize = Integer.MAX_VALUE;

        for (int mask = 1; mask < (1 << n); mask++) {
            Set<Resource> candidate = new HashSet<>();
            Set<String> covered = new HashSet<>();

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    Resource r = resourceList.get(i);
                    candidate.add(r);
                    covered.addAll(r.getKeywords());
                }
            }

            if (covered.containsAll(concepts) && candidate.size() < bestSize) {
                bestSize = candidate.size();
                bestSolution = candidate;
            }
        }

        return bestSolution != null ? bestSolution : new HashSet<>();
    }
}
