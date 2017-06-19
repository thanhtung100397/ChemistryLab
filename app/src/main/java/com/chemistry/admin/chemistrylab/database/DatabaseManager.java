package com.chemistry.admin.chemistrylab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;

import com.chemistry.admin.chemistrylab.chemical.gas.Gas;
import com.chemistry.admin.chemistrylab.chemical.liquid.Liquid;
import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionEquation;
import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionSubstance;
import com.chemistry.admin.chemistrylab.chemical.solid.Solid;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.Breaker;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.GasBottle;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.Jar;
import com.chemistry.admin.chemistrylab.fragment.PeriodicTableFragment;
import com.chemistry.admin.chemistrylab.tooltip.ItemTip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/10/2016.
 */
public class DatabaseManager {
    public static final String TAG = "DatabaseManager";
    private static final String APP_DATA_PATH = Environment.getDataDirectory().getPath() + "/data/com.chemistry.admin.chemistrylab/";

    private static final String DATABASE_FOLDER_NAME = "database";
    private static final String DATABASE_NAME = "database-test";
    private static final String DATABASE_DATA_PATH = Environment.getDataDirectory().getPath() + "/data/com.chemistry.admin.chemistrylab/" + DATABASE_FOLDER_NAME + "/" + DATABASE_NAME;

    public static final String SETTINGS = "settings";
    public static final String KEY_SPEED = "speed";

    public static final String REACTIONS_TABLE_NAME = "reactions";
    public static final String KEY_START = "start";
    public static final String KEY_RESULT = "result";
    public static final String KEY_SUBSTANCE_CONDITION = "substanceCondition";
    public static final String KEY_REQUIRE_CONDITION = "requireCondition";
    public static final String KEY_BALANCE_INDEX = "balanceIndex";
    public static final String KEY_RESULT_STATE = "resultState";

    public static final String SUBSTANCES_TABLE_NAME = "substances";
    public static final String KEY_SYMBOL = "symbol";
    public static final String KEY_NAME = "name";
    public static final String KEY_STATE = "state";
    public static final String KEY_COLOR = "color";
    public static final String KEY_M = "M";
    public static final String KEY_DENSITY = "density";
    public static final String KEY_WEIGHT_OR_VOLUME = "weightOrVolume";

    public static final String ELEMENTS_TABLE_NAME = "elements";
    public static final String KEY_MASS = "mass";
    public static final String KEY_BOILING = "boiling";
    public static final String KEY_MELTING = "melting";
    public static final String KEY_GROUPS = "groups";
    public static final String KEY_ATOMIC_NUMBER = "atomicNumber";
    public static final String KEY_ELECTRONIC_GRAVITY = "electronicGravity";
    public static final String KEY_ELECTRONIC_CONFIG = "electronicConfig";
    public static final String KEY_OXIDATION_STATES = "oxidationStates";

    public static final String BREAKER_MAP_VERTICAL_TABLE_NAME = "breaker_map_vertical";
    public static final String BREAKER_MAP_HORIZONTAL_TABLE_NAME = "breaker_map_horizontal";
    public static final String JAR_MAP_VERTICAL_TABLE_NAME = "jar_map_vertical";
    public static final String JAR_MAP_HORIZONTAL_TABLE_NAME = "jar_map_horizontal";
    public static final String GAS_BOTTLE_MAP_VERTICAL_TABLE_NAME = "gas_bottle_map_vertical";
    public static final String GAS_BOTTLE_MAP_HORIZONTAL_TABLE_NAME = "gas_bottle_map_horizontal";
    public static final String FLASK_MAP_VERTICAL_TABLE_NAME = "flask_map_vertical";
    public static final String FLASK_MAP_HORIZONTAL_TABLE_NAME = "flask_map_horizontal";
    public static final String TEST_TUBE_MAP_VERTICAL_TABLE_NAME = "test_tube_map_vertical";
    public static final String TEST_TUBE_MAP_HORIZONTAL_TABLE_NAME = "test_tube_map_horizontal";
    public static final String TROUGH_MAP_VERTICAL_TABLE_NAME = "trough_map_vertical";
    public static final String TROUGH_MAP_HORIZONTAL_TABLE_NAME = "trough_map_horizontal";
    public static final String CONICAL_FLASK_MAP_VERTICAL_TABLE_NAME = "conical_flask_map_vertical";
    public static final String CONICAL_FLASK_MAP_HORIZONTAL_TABLE_NAME = "conical_flask_map_horizontal";
    public static final String KEY_X = "x";
    public static final String KEY_X_START = "xStart";
    public static final String KEY_X_END = "xEnd";
    public static final String KEY_Y = "y";

    private Context context;
    private SQLiteDatabase database;
    public static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
            return instance;
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        this.context = context;
        copyDataToInternalStorage(DATABASE_FOLDER_NAME, DATABASE_NAME);
    }

    private void copyDataToInternalStorage(String folderName, String fileName) {
        if (!folderName.isEmpty()) {
            File file = new File(APP_DATA_PATH + folderName + "/");
            file.mkdir();
        }
        File fileDatabase = new File(APP_DATA_PATH + folderName + "/" + fileName);
        if (fileDatabase.exists()) {
            return;
        }
        try {
            DataInputStream inputStream = new DataInputStream(context.getAssets().open(folderName + "/" + fileName));
            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(fileDatabase));
            byte buff[] = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDatabase() {
        if (database == null || !database.isOpen()) {
            database = SQLiteDatabase.openDatabase(DATABASE_DATA_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    private void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public ReactionEquation findReaction(Substance substance1, Substance substance2, boolean hasHeat) {
        openDatabase();
        ReactionEquation result = new ReactionEquation();
        String searchStringCase1 = substance1.getSymbol() + "+" + substance2.getSymbol();
        String conditionCase1 = substance1.getState() + "_" + substance2.getState();
        String searchStringCase2 = substance2.getSymbol() + "+" + substance1.getSymbol();
        String conditionCase2 = substance2.getState() + "_" + substance1.getState();
        String balanceIndex[];


        Cursor cursor = database.rawQuery("SELECT * FROM " + REACTIONS_TABLE_NAME +
                        " WHERE " + KEY_START + " = \"" + searchStringCase1 + "\"" +
                        " AND " + KEY_SUBSTANCE_CONDITION + " = \"" + conditionCase1 + "\"",
                null);

        if (cursor.getCount() == 0) {
            cursor = database.rawQuery("SELECT * FROM " + REACTIONS_TABLE_NAME +
                            " WHERE " + KEY_START + " = \"" + searchStringCase2 + "\"" +
                            " AND " + KEY_SUBSTANCE_CONDITION + " = \"" + conditionCase2 + "\"",
                    null);
            if (cursor.getCount() == 0) {
                return null;
            } else {
                cursor.moveToFirst();
                balanceIndex = cursor.getString(cursor.getColumnIndex(KEY_BALANCE_INDEX)).split("_");
                result.addReactionSubstance(new ReactionSubstance(substance2,
                        Integer.parseInt(balanceIndex[0])));
                result.addReactionSubstance(new ReactionSubstance(substance1,
                        Integer.parseInt(balanceIndex[1])));
            }
        } else {
            cursor.moveToFirst();
            balanceIndex = cursor.getString(cursor.getColumnIndex(KEY_BALANCE_INDEX)).split("_");
            result.addReactionSubstance(new ReactionSubstance(substance1,
                    Integer.parseInt(balanceIndex[0])));
            result.addReactionSubstance(new ReactionSubstance(substance2,
                    Integer.parseInt(balanceIndex[1])));
        }

        if (cursor.getCount() == 2) {
            int requireConditionIndex = cursor.getColumnIndex(KEY_REQUIRE_CONDITION);
            if (hasHeat) {
                if (cursor.getString(requireConditionIndex) == null) {
                    cursor.moveToNext();
                }
            }
        }

        int resultColumnIndex = cursor.getColumnIndex(KEY_RESULT);
        int resultStateColumnIndex = cursor.getColumnIndex(KEY_RESULT_STATE);

        String buffSymbolResult[] = cursor.getString(resultColumnIndex).split("\\+");
        String buffStateResult[] = cursor.getString(resultStateColumnIndex).split("_");
        int length = buffSymbolResult.length;
        for (int i = 0; i < length; i++) {
            Substance substance = getSubstanceBySymbolAndState(buffSymbolResult[i], buffStateResult[i]);
            if (substance instanceof Gas) {
                result.setHasGasCreated(true);
            }
            result.addReactionSubstance(new ReactionSubstance(substance,
                    Integer.parseInt(balanceIndex[i + 2])));
        }
        result.setUpReaction();

        cursor.close();
        closeDatabase();
        return result;
    }

    public Substance getSubstanceBySymbolAndState(String symbol, String state) {
        Substance result = null;
        openDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SUBSTANCES_TABLE_NAME +
                        " WHERE " + KEY_SYMBOL + " = \"" + symbol + "\"" +
                        " AND " + KEY_STATE + " = \"" + state + "\"",
                null);
        if (cursor.getCount() == 0) {
            Log.i(TAG, "lack substance " + symbol + " in " + state + " state in database");
            return null;
        }
        cursor.moveToFirst();

        switch (state) {
            case "solid": {
                result = new Solid(cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        symbol,
                        cursor.getString(cursor.getColumnIndex(KEY_COLOR)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_M)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_DENSITY)),
                        0);
            }
            break;

            case "liquid": {
                result = new Liquid(cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        symbol,
                        cursor.getString(cursor.getColumnIndex(KEY_COLOR)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_M)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_DENSITY)),
                        0);
            }
            break;

            case "gas": {
                result = new Gas(cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        symbol,
                        cursor.getString(cursor.getColumnIndex(KEY_COLOR)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_M)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_DENSITY)),
                        0);
            }
            break;

            default: {
                break;
            }
        }
        cursor.close();
        closeDatabase();
        result.setTip(new ItemTip(context, result));
        return result;
    }

    public Point[] getArrayPointOf(String tableName) {
        openDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
        Point result[] = new Point[cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        int xStartColumnIndex = cursor.getColumnIndex(KEY_X_START);
        int xEndColumnIndex = cursor.getColumnIndex(KEY_X_END);
        while (!cursor.isAfterLast()) {
            result[i++] = new Point(cursor.getInt(xStartColumnIndex),
                    cursor.getInt(xEndColumnIndex));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return result;
    }

    public int getYByX(String mapHorizontalTableName, int x) {
        openDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + mapHorizontalTableName +
                " WHERE " + KEY_X + " = " + x, null);
        if (cursor.getCount() != 1) {
            Log.e(TAG, "method getYByX(): database data error, value: " + x);
            return -1;
        }
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex(KEY_Y));
        cursor.close();
        closeDatabase();
        return result;
    }

    public List<Substance> findSubstancesByName(String name) {
        openDatabase();
        List<Substance> result = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SUBSTANCES_TABLE_NAME +
                " WHERE " + KEY_NAME +
                " LIKE '" + name + "%'", null);
        cursor.moveToFirst();

        int columnNameIndex = cursor.getColumnIndex(KEY_NAME);
        int columnSymbolIndex = cursor.getColumnIndex(KEY_SYMBOL);
        int columnStateIndex = cursor.getColumnIndex(KEY_STATE);
        int columnColorIndex = cursor.getColumnIndex(KEY_COLOR);
        int columnMIndex = cursor.getColumnIndex(KEY_M);
        int columnDensityIndex = cursor.getColumnIndex(KEY_DENSITY);
        while (!cursor.isAfterLast()) {
            String state = cursor.getString(columnStateIndex);
            switch (state) {
                case "solid": {
                    result.add(new Solid(cursor.getString(columnNameIndex),
                            cursor.getString(columnSymbolIndex),
                            cursor.getString(columnColorIndex),
                            cursor.getDouble(columnMIndex),
                            cursor.getDouble(columnDensityIndex),
                            0));
                }
                break;

                case "liquid": {
                    result.add(new Liquid(cursor.getString(columnNameIndex),
                            cursor.getString(columnSymbolIndex),
                            cursor.getString(columnColorIndex),
                            cursor.getDouble(columnMIndex),
                            cursor.getDouble(columnDensityIndex),
                            0));
                }
                break;

                case "gas": {
                    result.add(new Gas(cursor.getString(columnNameIndex),
                            cursor.getString(columnSymbolIndex),
                            cursor.getString(columnColorIndex),
                            cursor.getDouble(columnMIndex),
                            cursor.getDouble(columnDensityIndex),
                            0));
                }
                break;

                default: {
                    break;
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return result;
    }

    public String[] getAllElementSymbol() {
        openDatabase();
        Cursor cursor = database.rawQuery("SELECT " + KEY_SYMBOL + " FROM " + ELEMENTS_TABLE_NAME, null);
        cursor.moveToFirst();
        String result[] = new String[cursor.getCount()];
        int index = 0;
        while (!cursor.isAfterLast()) {
            result[index++] = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return result;
    }

    public PeriodicTableFragment.ElementItem getElement(String symbol) {
        openDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + ELEMENTS_TABLE_NAME + " WHERE " + KEY_SYMBOL + " = '" + symbol + "'", null);
        cursor.moveToFirst();
        PeriodicTableFragment.ElementItem result =
                new PeriodicTableFragment.ElementItem(cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_SYMBOL)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_MASS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_ATOMIC_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(KEY_ELECTRONIC_CONFIG)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_ELECTRONIC_GRAVITY)),
                        cursor.getString(cursor.getColumnIndex(KEY_OXIDATION_STATES)));
        cursor.close();
        closeDatabase();
        return result;
    }

    public void updateWeightOrVolumeOf(Substance substance) {
        openDatabase();
        double weightOrVolume;
        if (substance instanceof Solid) {
            weightOrVolume = substance.getWeight();
        } else {
            weightOrVolume = substance.getVolume();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_WEIGHT_OR_VOLUME, weightOrVolume);
        database.update(SUBSTANCES_TABLE_NAME, contentValues, KEY_SYMBOL + "=? AND " + KEY_STATE + "=?", new String[]{substance.getSymbol(), substance.getState()});
        closeDatabase();
    }

//    public int[] getXByY(String mapVerticalTableName, int y) {
//        openDatabase();
//        Cursor cursor = database.rawQuery("SELECT * FROM " + mapVerticalTableName + " WHERE " + KEY_Y + " = " + y, null);
//        if (cursor.getCount() != 1) {
//            Log.e(TAG, "method getXByY(): database data error");
//            return null;
//        }
//        cursor.moveToFirst();
//        int result[] = new int[2];
//        int xColumnIndex = cursor.getColumnIndex(KEY_X);
//        result[0] = cursor.getInt(xColumnIndex);
//        cursor.moveToNext();
//        result[1] = cursor.getInt(xColumnIndex);
//        cursor.close();
//        closeDatabase();
//        return result;
//    }
//
//    public void insertToDataBase(String tableName, Point point[]) {
//        openDatabase();
//        for (Point aPoint : point) {
////            database.execSQL("INSERT INTO " + tableName + " (" + KEY_X + ", " + KEY_Y + ") VALUES (" + aPoint.x + ", " + aPoint.y + ")", null);
//            ContentValues value = new ContentValues();
////            value.put(KEY_X,aPoint.x);
////            value.put(KEY_Y,aPoint.y);
//            value.put("xStart", aPoint.x);
//            value.put("xEnd", aPoint.y);
//            database.insert(tableName, null, value);
////            if (result != -1) {
////                Log.i(TAG, "Insert completed");
////            } else {
////                Log.i(TAG, "Insert error");
////            }
//        }
//        closeDatabase();
//    }

    public List<Breaker> getAllLiquidPreview() {
        openDatabase();
        List<Breaker> listResult = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SUBSTANCES_TABLE_NAME +
                        " WHERE " + KEY_STATE + " = \"liquid\"",
                null);
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex(KEY_NAME);
        int symbolColumnIndex = cursor.getColumnIndex(KEY_SYMBOL);
        int colorColumnIndex = cursor.getColumnIndex(KEY_COLOR);
        int MColumnIndex = cursor.getColumnIndex(KEY_M);
        int densityColumnIndex = cursor.getColumnIndex(KEY_DENSITY);
        int weightOrVolumeIndex = cursor.getColumnIndex(KEY_WEIGHT_OR_VOLUME);
        while (!cursor.isAfterLast()) {
            Liquid liquid = new Liquid(cursor.getString(nameColumnIndex),
                    cursor.getString(symbolColumnIndex),
                    cursor.getString(colorColumnIndex),
                    cursor.getDouble(MColumnIndex),
                    cursor.getDouble(densityColumnIndex),
                    cursor.getDouble(weightOrVolumeIndex));
            liquid.setTip(new ItemTip(context, liquid));
            Breaker breaker = new Breaker(context, Breaker.BREAKER_STANDARD_WIDTH, Breaker.BREAKER_STANDARD_HEIGHT);
            breaker.addSubstance(liquid);
            breaker.invalidate();
            listResult.add(breaker);
            cursor.moveToNext();
        }
        closeDatabase();
        cursor.close();
        return listResult;
    }

    public List<Jar> getAllSolidPreview() {
        openDatabase();
        List<Jar> listResult = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SUBSTANCES_TABLE_NAME +
                        " WHERE " + KEY_STATE + " = \"solid\"",
                null);
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex(KEY_NAME);
        int symbolColumnIndex = cursor.getColumnIndex(KEY_SYMBOL);
        int colorColumnIndex = cursor.getColumnIndex(KEY_COLOR);
        int MColumnIndex = cursor.getColumnIndex(KEY_M);
        int densityColumnIndex = cursor.getColumnIndex(KEY_DENSITY);
        int weightOrVolumeIndex = cursor.getColumnIndex(KEY_WEIGHT_OR_VOLUME);
        while (!cursor.isAfterLast()) {
            Solid solid = new Solid(cursor.getString(nameColumnIndex),
                    cursor.getString(symbolColumnIndex),
                    cursor.getString(colorColumnIndex),
                    cursor.getDouble(MColumnIndex),
                    cursor.getDouble(densityColumnIndex),
                    cursor.getDouble(weightOrVolumeIndex));
            solid.setTip(new ItemTip(context, solid));
            Jar jar = new Jar(context, Jar.JAR_STANDARD_WIDTH, Jar.JAR_STANDARD_HEIGHT);
            jar.addSubstance(solid);
            jar.invalidate();
            listResult.add(jar);
            cursor.moveToNext();
        }
        closeDatabase();
        cursor.close();
        return listResult;
    }

    public List<GasBottle> getAllGasPreview() {
        openDatabase();
        List<GasBottle> listResult = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SUBSTANCES_TABLE_NAME +
                        " WHERE " + KEY_STATE + " = \"gas\"",
                null);
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex(KEY_NAME);
        int symbolColumnIndex = cursor.getColumnIndex(KEY_SYMBOL);
        int colorColumnIndex = cursor.getColumnIndex(KEY_COLOR);
        int MColumnIndex = cursor.getColumnIndex(KEY_M);
        int densityColumnIndex = cursor.getColumnIndex(KEY_DENSITY);
        int weightOrVolumeIndex = cursor.getColumnIndex(KEY_WEIGHT_OR_VOLUME);
        while (!cursor.isAfterLast()) {
            Gas gas = new Gas(cursor.getString(nameColumnIndex),
                    cursor.getString(symbolColumnIndex),
                    cursor.getString(colorColumnIndex),
                    cursor.getDouble(MColumnIndex),
                    cursor.getDouble(densityColumnIndex),
                    cursor.getDouble(weightOrVolumeIndex));
            gas.setTip(new ItemTip(context, gas));
            GasBottle gasBottle = new GasBottle(context, GasBottle.GAS_BOTTLE_STANDARD_WIDTH, GasBottle.GAS_BOTTLE_STANDARD_HEIGHT);
            gasBottle.addSubstance(gas);
            gasBottle.invalidate();
            listResult.add(gasBottle);
            cursor.moveToNext();
        }
        closeDatabase();
        cursor.close();
        return listResult;
    }
}
