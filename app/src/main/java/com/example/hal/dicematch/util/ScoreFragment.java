package com.example.hal.dicematch.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hal.dicematch.HighScoreActivity;
import com.example.hal.dicematch.R;
import com.example.hal.dicematch.ScoreHelpActivity;
import com.example.hal.dicematch.util.ListViewAdapter;
import com.example.hal.dicematch.util.Player;

import org.xmlpull.v1.XmlPullParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreFragment extends Fragment implements AbsListView.OnItemClickListener, Serializable {

    private ArrayList<Integer> dices;
    OnNextRoundListener oListener;


    public interface OnNextRoundListener {
        void onNextRoundThrow();
        boolean writeRow(int row, ArrayList<Integer> dices);
        Integer getScore();
        ArrayList<Boolean> getFinished();
        Integer newTotalScore();
        boolean getBonus1();
        void onExitGame();
        Boolean isTimeToWriteScore();
        void setTimeToWriteScore(Boolean timeToWriteScore);
        int getBonus2();
        void endGame(String s);
    }

    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN = "Choice";
    public static final String SECOND_COLUMN = "Score";
    public static ArrayList<String> categories2;
    List<Integer> scoreList = new ArrayList<>();
    List<Boolean> writableList = new ArrayList<>();
    int roundNumber = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void refreshScoreFragment(Player actualPlayer, XmlResourceParser xmlParser) {
        this.scoreList = actualPlayer.getFullScore();
        this.list = new ArrayList<>();
        categories2 = readXMLdata(xmlParser);
        for (int i = 0; i < categories2.size(); i++) {
            HashMap<String, String> temp = new HashMap<>();
            temp.put(FIRST_COLUMN, categories2.get(i));
            temp.put(SECOND_COLUMN, scoreList.get(i).toString());
            this.list.add(temp);
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories2 = readXMLdata(getActivity().getResources().getXml(R.xml.categories));
        for (int i = 0; i < categories2.size(); i++) {
            scoreList.add(0);
        }
    }

    private ArrayList<String> readXMLdata(XmlResourceParser xmlParser) {
        ArrayList<String> categ = new ArrayList<>();
        try {
            xmlParser.next();
            int eventType = xmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG
                            && xmlParser.getName().equalsIgnoreCase("name")) {
                        xmlParser.next();
                        String cat = xmlParser.getText();
                        //int intValue = xrp.getAttributeIntValue(null, "order", 0);
                        //Log.d("INFO", cat);
                        categ.add(cat);
                    }
                eventType = xmlParser.next();
            }
        } catch (Exception e) {
            Log.e("INFO", "readXML chyba" + e.toString());
        }
        return categ;
    }

//    public void onConfigurationChanged(Configuration newConfig) {
//   /* Super... */
//        super.onConfigurationChanged(newConfig);
//        Log.d("INFO", "changes score orient");
//   /* Re-create view... */
//        ViewGroup viewGroup = (ViewGroup) getView();
//        viewGroup.removeAllViewsInLayout();
//        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null); viewGroup.addView(view);
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View scoreScreen = inflater.inflate(R.layout.fragment_item_list, container, false);

        scoreScreen.findViewById(R.id.saveGame).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oListener.onExitGame();
            }
        });

        scoreScreen.findViewById(R.id.help_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ScoreHelpActivity.class);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        final ListView listView = (ListView) scoreScreen.findViewById(R.id.scoreList);
        list = new ArrayList<>();
        for (int i = 0; i < categories2.size(); i++) {
            HashMap<String, String> temp = new HashMap<>();
            temp.put(FIRST_COLUMN, categories2.get(i));
            temp.put(SECOND_COLUMN, scoreList.get(i).toString());
            list.add(temp);
        }

        ListViewAdapter adapter = new ListViewAdapter(oListener.getFinished(), getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if ((oListener.isTimeToWriteScore()) && (oListener.writeRow(position, dices))) {
                    try {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(FIRST_COLUMN, categories2.get(position));
                        temp.put(SECOND_COLUMN, oListener.getScore().toString());
                        HashMap<String, String> temp2 = new HashMap<>();
                        temp2.put(FIRST_COLUMN, categories2.get(categories2.size() - 1));
                        if (oListener.getBonus1()) {
                            HashMap<String, String> temp3 = new HashMap<>();
                            temp3.put(FIRST_COLUMN, categories2.get(6));
                            temp3.put(SECOND_COLUMN, "" + 30);
                            list.set(6, temp3);
                        }
                        if (oListener.getBonus2() > 1) {
                            HashMap<String, String> temp4 = new HashMap<>();
                            temp4.put(FIRST_COLUMN, categories2.get(13));
                            temp4.put(SECOND_COLUMN, "" + (100 * (oListener.getBonus2() - 1)));
                            list.set(13, temp4);
                        }
                        temp2.put(SECOND_COLUMN, oListener.newTotalScore().toString());
                        list.set(position, temp);
                        list.set(categories2.size() - 1, temp2);
                        ListViewAdapter adapter = new ListViewAdapter(oListener.getFinished(), getActivity(), list);
                        listView.setAdapter(adapter);
                        roundNumber++;
                        oListener.setTimeToWriteScore(false);
                        oListener.onNextRoundThrow();


                    } catch (Exception e) {
                        Log.d("INFO", e.toString());
                    }
                }
                //int pos = position + 1;
                //Toast.makeText(getActivity(), Integer.toString(pos) + " Clicked", Toast.LENGTH_SHORT).show();
            }

        });
        return scoreScreen;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            oListener = (OnNextRoundListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public void saveRoll(ArrayList<Integer> dices) {
        oListener.setTimeToWriteScore(true);
        setValues (dices);
    }

    public void setValues(ArrayList<Integer> dices) {
        this.dices = dices;
    }

    public ArrayList<HashMap<String, String>> getList() {
        return list;
    }

    public void setList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }


}
