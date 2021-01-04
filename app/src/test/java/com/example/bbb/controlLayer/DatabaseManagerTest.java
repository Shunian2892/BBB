package com.example.bbb.controlLayer;

import com.example.bbb.controlLayer.gps.OpenRouteService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    @Mock
    private DatabaseManager databaseManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void initDatabase() {
    }

    @Test
    public void readJson() {
    }

    @Test
    public void loadJSONFromFile() {
    }

    @Test
    public void testQueries() {
    }

    @Test
    public void getPOIs() {
    }

    @Test
    public void getRoutes() {
    }

    @Test
    public void getPOIsFromRoute() {
    }

    @Test
    public void addWalkedRoute() {
    }

    @Test
    public void getWalkedRoutes() {
    }

    @Test
    public void searchLocation() {
    }
}