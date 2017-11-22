package com.example.jiahang.pvrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.jiahang.pvrm.InstructionActivity.EXTRA_FOREARM_LENGTH;

public class InstructionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER CONFLICTING CHANGE
    private static final String ARG_PARAM1 = "whichInstruction";

    //THIS IS ANOTHER TEST

    ImageView topPic;
    ImageView botPic;
    TextView instruction_one;
    TextView instruction_two;
    LinearLayout linearLayout_forearm_length;
    EditText editText_forearm_length;
    Button button_next;

    int instruction_key;

    int[] pic_resource_ids = {R.drawable.step_1,R.drawable.step_2, R.drawable.step_3, R.drawable.step_4_1,
                                R.drawable.step_4_2, R.drawable.step_5, R.drawable.step_6, R.drawable.step_7, R.drawable.placeholder};
    String[] instruction_array_text = {"Wipe with alcohol wipe (or dab with medical grade tape if there is too much dead skin cells) on the highlighted area. If the skin is too dry, apply small amount of electro-gel in the area.",
                                        "Attach adhesive electrodes on the EMG sensors & reference electrode.",
                                        "Attach the sensors & electrode shown above: Biceps sensor-belly of biceps, Triceps sensor-longhead of triceps, Ref.electrode-clavicle near sternal notch",
                                        "Turn on the PVRM module. Then, put PVRM main and moving module in the calibration position as shown above.",
                                        "Attach wrist support brace for all users (except those with sever wrist contractions). Attach the moving module on the radial side of the subject’s wrist.",
                                        "Attach the main module on the subject’s upper-arm. Connect the EMG cable to the main module.",
                                        "Measure the forearm length (distance between the marked line to the elbow epicondyle)"};


    public static InstructionFragment newInstance(int whichInstruction) {
        InstructionFragment fragment = new InstructionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, whichInstruction);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            instruction_key = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instruction, container, false);
        topPic = (ImageView)v.findViewById(R.id.ID_image_instruction_top);
        botPic = (ImageView)v.findViewById(R.id.ID_image_instruction_bot);
        instruction_one = (TextView)v.findViewById(R.id.ID_textview_instruction_top);
        instruction_two = (TextView)v.findViewById(R.id.ID_textview_instruction_bot);
        linearLayout_forearm_length = (LinearLayout)v.findViewById(R.id.ID_linearlayout_forearm_length);

        if(instruction_key < 3) {
            topPic.setImageResource(pic_resource_ids[instruction_key]);
            //botPic.setImageResource(pic_resource_ids[pic_resource_ids.length-1]);
            botPic.setVisibility(View.GONE);
            instruction_one.setText(instruction_array_text[instruction_key]);
            instruction_two.setText("");
        }
        else if(instruction_key == 3) {
            topPic.setImageResource(pic_resource_ids[instruction_key]);
            botPic.setImageResource(pic_resource_ids[instruction_key+1]);
            instruction_one.setVisibility(View.GONE);
            instruction_two.setText(instruction_array_text[instruction_key]);
        }

        else {
            topPic.setImageResource(pic_resource_ids[instruction_key+1]);
            //botPic.setImageResource(pic_resource_ids[pic_resource_ids.length-1]);
            botPic.setVisibility(View.GONE);
            instruction_one.setText(instruction_array_text[instruction_key]);
            instruction_two.setText("");
            // last instruction
            if(instruction_key == 6) {
                editText_forearm_length = (EditText)v.findViewById(R.id.ID_editText_forearm_length);
                linearLayout_forearm_length.setVisibility(View.VISIBLE);

                button_next = (Button)v.findViewById(R.id.ID_button_next);
                button_next.setVisibility(View.VISIBLE);
                button_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtras(getActivity().getIntent());
                        intent.putExtra(EXTRA_FOREARM_LENGTH, editText_forearm_length.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        }

        return v;

    }
}
