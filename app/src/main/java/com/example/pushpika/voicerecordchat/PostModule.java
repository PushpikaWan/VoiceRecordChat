package com.example.pushpika.voicerecordchat;

/**
 * Created by pushpika on 8/3/16.
 */
public class PostModule {

    String cardName,cardName2,cardName3,cardName4,cardName5,cardName6;

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
    public String getCardName3() {
        return cardName3;
    }
    public String getCardName4() {
        return cardName4;
    }
    public String getCardName5() {
        return cardName5;
    }
    public String getCardName6() {
        return cardName6;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public void setCardName2(String cardName2) {
        this.cardName2 = cardName2;
    }
    public void setCardName3(String cardName3) {
        this.cardName3 = cardName3;
    }
    public void setCardName4(String cardName4) {this.cardName4 = cardName4; }
    public void setCardName5(String cardName5) {this.cardName5 = cardName5; }
    public void setCardName6(String cardName6) {this.cardName6 = cardName6; }


    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
