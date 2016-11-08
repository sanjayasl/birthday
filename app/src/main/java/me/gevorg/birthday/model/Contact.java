package me.gevorg.birthday.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Model class for contact.
 *
 * @author Gevorg Harutyunyan.
 */
public class Contact implements Comparable<Contact> {
    // Lookup key.
    private String lookupKey;

    // Id.
    private int id;

    // Contact.
    private Date birthday;

    // Name.
    private String name;

    // Photo.
    private String photo;

    // Age.
    private int age;

    // Days till birthday.
    private int days;

    /**
     * Short constructor.
     *
     * @param lookupKey lookup key.
     * @param birthday birthday.
     */
    public Contact(String lookupKey, Date birthday) {
        setLookupKey(lookupKey);
        setBirthday(birthday);
    }

    /**
     * Full-arg constructor.
     *
     * @param lookupKey lookup key.
     * @param id id.
     * @param birthday birthday.
     * @param name name.
     * @param photo photo.
     */
    public Contact(String lookupKey, int id, Date birthday, String name, String photo) {
        setLookupKey(lookupKey);
        setId(id);
        setBirthday(birthday);
        setName(name);
        setPhoto(photo);
    }

    /**
     * Getter for lookup key.
     *
     * @return lookup key.
     */
    public String getLookupKey() {
        return lookupKey;
    }

    /**
     * Setter for lookup key.
     *
     * @param lookupKey lookup key.
     */
    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    /**
     * Getter for id.
     *
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for birthday.
     *
     * @return birthday.
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Setter for birthday.
     *
     * @param birthday birthday.
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;

        // Calculate age and days.
        calculateAge();
    }

    /**
     * Getter for name.
     *
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     *
     * @param name name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for photo.
     *
     * @return photo.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Setter for photo.
     *
     * @param photo photo.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns age.
     *
     * @return age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns days.
     *
     * @return days.
     */
    public int getDays() {
        return days;
    }

    /**
     * Used to sync age and days fields.
     */
    private void calculateAge() {
        // Today.
        Calendar now = Calendar.getInstance();

        // Birthday.
        Calendar birthdayCal = Calendar.getInstance();
        birthdayCal.setTime(birthday);

        // Age.
        age = Math.max(now.get(Calendar.YEAR) - birthdayCal.get(Calendar.YEAR), 1);
        birthdayCal.set(Calendar.YEAR, now.get(Calendar.YEAR));

        // Days.
        days = birthdayCal.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
        if (days < 0) {
            days = now.getActualMaximum(Calendar.DAY_OF_YEAR) + days;
            age ++;
        }
    }

    /**
     * Comparing method.
     *
     * @param another contact.
     * @return diff in days till birthday.
     */
    @Override
    public int compareTo(Contact another) {
        return getDays() - another.getDays();
    }
}
