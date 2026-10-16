package com.example.skilltree.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.model.UserProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SkillTreeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "skilltree_ads.db";
    private static final int DATABASE_VERSION = 1;

    private static SkillTreeDbHelper instance;
    private final Gson gson = new Gson();

    public static final String TABLE_TREES = "skill_trees";
    public static final String COL_TREE_ID = "id";
    public static final String COL_TREE_TITLE = "title";
    public static final String COL_TREE_DESC = "description";
    public static final String COL_TREE_LEVEL = "target_level";
    public static final String COL_TREE_OBJ = "objective";
    public static final String COL_TREE_TIME = "daily_time";
    public static final String COL_TREE_XP = "total_xp";
    public static final String COL_TREE_CREATED = "created_at";

    public static final String TABLE_SKILLS = "skills";
    public static final String COL_SKILL_ID = "id";
    public static final String COL_SKILL_TREE_ID = "tree_id";
    public static final String COL_SKILL_EXT_ID = "ext_id";
    public static final String COL_SKILL_NAME = "name";
    public static final String COL_SKILL_CAT = "category";
    public static final String COL_SKILL_DESC = "description";
    public static final String COL_SKILL_LEVEL = "level";
    public static final String COL_SKILL_XP = "xp_reward";
    public static final String COL_SKILL_STATUS = "status";
    public static final String COL_SKILL_RESOURCES = "study_resources_json";

    public static final String TABLE_PREREQUISITES = "skill_prerequisites";
    public static final String COL_PREREQ_ID = "id";
    public static final String COL_PREREQ_SKILL_ID = "skill_id";
    public static final String COL_PREREQ_TREE_ID = "tree_id";
    public static final String COL_PREREQ_REQUIRED_ID = "required_id";

    public static final String TABLE_PROFILE = "user_profile";
    public static final String COL_PROFILE_ID = "id";
    public static final String COL_PROFILE_XP = "total_xp";
    public static final String COL_PROFILE_COMPLETED = "completed_skills_count";

    public static synchronized SkillTreeDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SkillTreeDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private SkillTreeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TREES + " (" +
                COL_TREE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TREE_TITLE + " TEXT NOT NULL, " +
                COL_TREE_DESC + " TEXT, " +
                COL_TREE_LEVEL + " TEXT, " +
                COL_TREE_OBJ + " TEXT, " +
                COL_TREE_TIME + " TEXT, " +
                COL_TREE_XP + " INTEGER DEFAULT 0, " +
                COL_TREE_CREATED + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_SKILLS + " (" +
                COL_SKILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SKILL_TREE_ID + " INTEGER NOT NULL, " +
                COL_SKILL_EXT_ID + " INTEGER NOT NULL, " +
                COL_SKILL_NAME + " TEXT NOT NULL, " +
                COL_SKILL_CAT + " TEXT, " +
                COL_SKILL_DESC + " TEXT, " +
                COL_SKILL_LEVEL + " INTEGER DEFAULT 1, " +
                COL_SKILL_XP + " INTEGER DEFAULT 100, " +
                COL_SKILL_STATUS + " TEXT NOT NULL, " +
                COL_SKILL_RESOURCES + " TEXT, " +
                "FOREIGN KEY (" + COL_SKILL_TREE_ID + ") REFERENCES " + TABLE_TREES + "(" + COL_TREE_ID + ") ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE " + TABLE_PREREQUISITES + " (" +
                COL_PREREQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PREREQ_TREE_ID + " INTEGER NOT NULL, " +
                COL_PREREQ_SKILL_ID + " INTEGER NOT NULL, " +
                COL_PREREQ_REQUIRED_ID + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_PROFILE + " (" +
                COL_PROFILE_ID + " INTEGER PRIMARY KEY, " +
                COL_PROFILE_XP + " INTEGER DEFAULT 0, " +
                COL_PROFILE_COMPLETED + " INTEGER DEFAULT 0)");

        ContentValues cv = new ContentValues();
        cv.put(COL_PROFILE_ID, 1);
        cv.put(COL_PROFILE_XP, 0);
        cv.put(COL_PROFILE_COMPLETED, 0);
        db.insert(TABLE_PROFILE, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREREQUISITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SKILLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(db);
    }

    public long insertSkillTree(SkillTree tree) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long treeId = -1;
        try {
            ContentValues cvTree = new ContentValues();
            cvTree.put(COL_TREE_TITLE, tree.getTitle());
            cvTree.put(COL_TREE_DESC, tree.getDescription());
            cvTree.put(COL_TREE_LEVEL, tree.getTargetLevel());
            cvTree.put(COL_TREE_OBJ, tree.getObjective());
            cvTree.put(COL_TREE_TIME, tree.getDailyTime());
            cvTree.put(COL_TREE_XP, tree.getTotalXp());
            cvTree.put(COL_TREE_CREATED, tree.getCreatedAt() > 0 ? tree.getCreatedAt() : System.currentTimeMillis());

            treeId = db.insert(TABLE_TREES, null, cvTree);
            tree.setId(treeId);

            if (tree.getSkills() != null) {
                for (Skill skill : tree.getSkills()) {
                    ContentValues cvSkill = new ContentValues();
                    cvSkill.put(COL_SKILL_TREE_ID, treeId);
                    cvSkill.put(COL_SKILL_EXT_ID, skill.getId());
                    cvSkill.put(COL_SKILL_NAME, skill.getName());
                    cvSkill.put(COL_SKILL_CAT, skill.getCategory());
                    cvSkill.put(COL_SKILL_DESC, skill.getDescription());
                    cvSkill.put(COL_SKILL_LEVEL, skill.getLevel());
                    cvSkill.put(COL_SKILL_XP, skill.getXpReward());
                    cvSkill.put(COL_SKILL_STATUS, skill.getStatus().name());
                    cvSkill.put(COL_SKILL_RESOURCES, gson.toJson(skill.getStudyResources()));

                    db.insert(TABLE_SKILLS, null, cvSkill);

                    if (skill.getPrerequisites() != null) {
                        for (Integer reqId : skill.getPrerequisites()) {
                            ContentValues cvPre = new ContentValues();
                            cvPre.put(COL_PREREQ_TREE_ID, treeId);
                            cvPre.put(COL_PREREQ_SKILL_ID, skill.getId());
                            cvPre.put(COL_PREREQ_REQUIRED_ID, reqId);
                            db.insert(TABLE_PREREQUISITES, null, cvPre);
                        }
                    }
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return treeId;
    }

    public List<SkillTree> getAllSkillTrees() {
        List<SkillTree> trees = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TREES, null, null, null, null, null, COL_TREE_CREATED + " DESC");

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    SkillTree tree = new SkillTree();
                    tree.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_TREE_ID)));
                    tree.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_TITLE)));
                    tree.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_DESC)));
                    tree.setTargetLevel(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_LEVEL)));
                    tree.setObjective(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_OBJ)));
                    tree.setDailyTime(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_TIME)));
                    tree.setTotalXp(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TREE_XP)));
                    tree.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_TREE_CREATED)));

                    tree.setSkills(getSkillsForTree(db, tree.getId()));
                    trees.add(tree);
                }
            } finally {
                cursor.close();
            }
        }
        return trees;
    }

    public SkillTree getSkillTreeById(long treeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TREES, null, COL_TREE_ID + " = ?",
                new String[]{String.valueOf(treeId)}, null, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    SkillTree tree = new SkillTree();
                    tree.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_TREE_ID)));
                    tree.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_TITLE)));
                    tree.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_DESC)));
                    tree.setTargetLevel(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_LEVEL)));
                    tree.setObjective(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_OBJ)));
                    tree.setDailyTime(cursor.getString(cursor.getColumnIndexOrThrow(COL_TREE_TIME)));
                    tree.setTotalXp(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TREE_XP)));
                    tree.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_TREE_CREATED)));

                    tree.setSkills(getSkillsForTree(db, treeId));
                    return tree;
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    private List<Skill> getSkillsForTree(SQLiteDatabase db, long treeId) {
        List<Skill> skills = new ArrayList<>();
        Cursor cursor = db.query(TABLE_SKILLS, null, COL_SKILL_TREE_ID + " = ?",
                new String[]{String.valueOf(treeId)}, null, null, COL_SKILL_LEVEL + " ASC, " + COL_SKILL_EXT_ID + " ASC");

        Type listType = new TypeToken<ArrayList<String>>(){}.getType();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    Skill skill = new Skill();
                    skill.setTreeId(treeId);
                    skill.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SKILL_EXT_ID)));
                    skill.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_SKILL_NAME)));
                    skill.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_SKILL_CAT)));
                    skill.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_SKILL_DESC)));
                    skill.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SKILL_LEVEL)));
                    skill.setXpReward(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SKILL_XP)));

                    String statusStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_SKILL_STATUS));
                    try {
                        skill.setStatus(SkillStatus.valueOf(statusStr));
                    } catch (Exception e) {
                        skill.setStatus(SkillStatus.LOCKED);
                    }

                    String resJson = cursor.getString(cursor.getColumnIndexOrThrow(COL_SKILL_RESOURCES));
                    if (resJson != null && !resJson.isEmpty()) {
                        List<String> resources = gson.fromJson(resJson, listType);
                        skill.setStudyResources(resources);
                    }

                    skills.add(skill);
                }
            } finally {
                cursor.close();
            }
        }

        for (Skill s : skills) {
            s.setPrerequisites(getPrerequisitesForSkill(db, treeId, s.getId()));
        }

        return skills;
    }

    private List<Integer> getPrerequisitesForSkill(SQLiteDatabase db, long treeId, int skillExtId) {
        List<Integer> prereqs = new ArrayList<>();
        Cursor cursor = db.query(TABLE_PREREQUISITES, new String[]{COL_PREREQ_REQUIRED_ID},
                COL_PREREQ_TREE_ID + " = ? AND " + COL_PREREQ_SKILL_ID + " = ?",
                new String[]{String.valueOf(treeId), String.valueOf(skillExtId)},
                null, null, null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    prereqs.add(cursor.getInt(0));
                }
            } finally {
                cursor.close();
            }
        }
        return prereqs;
    }

    public void updateSkillStatus(long treeId, int skillExtId, SkillStatus newStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SKILL_STATUS, newStatus.name());
        db.update(TABLE_SKILLS, cv,
                COL_SKILL_TREE_ID + " = ? AND " + COL_SKILL_EXT_ID + " = ?",
                new String[]{String.valueOf(treeId), String.valueOf(skillExtId)});
    }

    public void deleteSkillTree(long treeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PREREQUISITES, COL_PREREQ_TREE_ID + " = ?", new String[]{String.valueOf(treeId)});
            db.delete(TABLE_SKILLS, COL_SKILL_TREE_ID + " = ?", new String[]{String.valueOf(treeId)});
            db.delete(TABLE_TREES, COL_TREE_ID + " = ?", new String[]{String.valueOf(treeId)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public UserProfile getUserProfile() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROFILE, null, COL_PROFILE_ID + " = 1", null, null, null, null);
        UserProfile profile = new UserProfile();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    profile.setTotalXp(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROFILE_XP)));
                    profile.setCompletedSkillsCount(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROFILE_COMPLETED)));
                }
            } finally {
                cursor.close();
            }
        }

        Cursor countCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TREES, null);
        if (countCursor != null) {
            try {
                if (countCursor.moveToFirst()) {
                    profile.setActiveTreesCount(countCursor.getInt(0));
                }
            } finally {
                countCursor.close();
            }
        }

        return profile;
    }

    public void addXp(int xpGained) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_PROFILE + " SET " +
                COL_PROFILE_XP + " = " + COL_PROFILE_XP + " + " + xpGained + ", " +
                COL_PROFILE_COMPLETED + " = " + COL_PROFILE_COMPLETED + " + 1 WHERE " +
                COL_PROFILE_ID + " = 1");
    }
}
