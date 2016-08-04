package com.example.pushpika.voicerecordchat;

/**
 * Created by pushpika on 8/3/16.
 */
public class PostModule {

    String cardName,cardName2;
    int imageResourceId;
    int isfav;
    int isturned;

    public int getIsturned() {
        return isturned;
    }

    public void setIsturned(int isturned) {
        this.isturned = isturned;
    }

    public int getIsfav() {
        return isfav;
    }

    public void setIsfav(int isfav) {
        this.isfav = isfav;
    }

    public String getCardName() {
        return cardName;
    }
    public String getCardName2() {
        return cardName2;
    }


    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public void setCardName2(String cardName2) {
        this.cardName2 = cardName2;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
