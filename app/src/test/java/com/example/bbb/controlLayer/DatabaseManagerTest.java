package com.example.bbb.controlLayer;

import android.content.Context;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.App;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseManagerTest {

    @Mock
    private DatabaseManager databaseManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        databaseManager = mock(DatabaseManager.class);
    }

    @Test
    public void getInstance() {
        setMock(databaseManager);
        DatabaseManager databaseManagerActual = DatabaseManager.getInstance();
        System.out.println(databaseManagerActual);
        assertNotNull(databaseManagerActual);
    }

    private void setMock(DatabaseManager mock) {
        try {
            Field instance = DatabaseManager.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}