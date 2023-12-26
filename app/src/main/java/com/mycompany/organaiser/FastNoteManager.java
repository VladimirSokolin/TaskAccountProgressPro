package com.mycompany.organaiser;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FastNoteManager {
    DaoFastNote daoFastNote;
    DaoRemindersFastNote daoReminders;
    public FastNoteManager(MyDatabaseOpenHelper dataHelper) {
        daoFastNote = new DaoFastNote(dataHelper);
        daoReminders = new DaoRemindersFastNote(dataHelper);
    }

    public FastNote insert(FastNote fastNote) {
        daoFastNote.insert(fastNote);
        for(RemindersFastNote reminder : fastNote.reminders) {
            reminder.idFastNote = fastNote.id;
            daoReminders.insert(reminder);
        }
        return fastNote;
    }

    public FastNote insert(String value, String title, Date date) {
        FastNote fastNote = new FastNote();
        fastNote.value = value;
        fastNote.title = title;
        fastNote.id = daoFastNote.insert(fastNote);
        RemindersFastNote remindersFastNote = new RemindersFastNote();
        remindersFastNote.date = date.getTime();
        remindersFastNote.idFastNote = fastNote.id;
        remindersFastNote.id = daoReminders.insert(remindersFastNote);
        fastNote.reminders.add(remindersFastNote);
        return fastNote;
    }

    public FastNote insert(String value, String title, List<Date> dates) {
        FastNote fastNote = new FastNote();
        fastNote.value = value;
        fastNote.title = title;
        fastNote.id = daoFastNote.insert(fastNote);
        for(Date date : dates) {
            RemindersFastNote remindersFastNote = new RemindersFastNote();
            remindersFastNote.date = date.getTime();
            remindersFastNote.idFastNote = fastNote.id;
            remindersFastNote.id = daoReminders.insert(remindersFastNote);
            fastNote.reminders.add(remindersFastNote);
        }
        return fastNote;
    }

    public ArrayList<FastNote> getAllCurrentDate() {
        ArrayList<RemindersFastNote> remindersAllCurrentDate = daoReminders.getAllCurrentDate();
        ArrayList<Long> ids = new ArrayList<>();
        for(RemindersFastNote reminder : remindersAllCurrentDate) {
            ids.add(reminder.idFastNote);
        }
        ArrayList<FastNote> fastNotes = daoFastNote.getAllById(ids);
        for(FastNote fastNote : fastNotes) {
            ArrayList<RemindersFastNote> listReminders = daoReminders.getAllById(fastNote.id);
            fastNote.reminders = listReminders;
        }
        return fastNotes;
    }

    public ArrayList<FastNote> getAllUpToThisDate(){
        ArrayList<RemindersFastNote> remindersAllUpToThisDate = daoReminders.getAllUpToThisDate();
        ArrayList<Long> ids = new ArrayList<>();
        for(RemindersFastNote reminder : remindersAllUpToThisDate) {
            ids.add(reminder.idFastNote);
        }
        ArrayList<FastNote> fastNotes = daoFastNote.getAllById(ids);
        for(FastNote fastNote : fastNotes) {
            ArrayList<RemindersFastNote> listReminders = daoReminders.getAllById(fastNote.id);
            fastNote.reminders = listReminders;
        }
        return fastNotes;
    }

    public void delete(long id) {
        daoFastNote.delete(id);
        daoReminders.deleteAllByIdNote(id);
    }

    public ArrayList<FastNote> getAll() {
        ArrayList<FastNote> listNotes = daoFastNote.getAll();
        for(FastNote note : listNotes) {
            ArrayList<RemindersFastNote> listReminders = daoReminders.getAllById(note.id);
            for(RemindersFastNote reminder : listReminders) {
                note.reminders.add(reminder);
            }
        }
        return listNotes;
    }



    public void deleteReminder(long id) {
        daoReminders.delete(id);
    }
}
