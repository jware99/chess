package dataaccess;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<Integer, Pet> users = new HashMap<>();
}
