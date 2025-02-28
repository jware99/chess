package result;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;


public record ListGamesResult(ArrayList<GameData> games) {
}
