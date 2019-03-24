package c17sal.cs.umu.se.blackjacklab3.controller.gameActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import c17sal.cs.umu.se.blackjacklab3.R;

public class InfoFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_info,container,false);

        Button hitButton = (Button) view.findViewById(R.id.hitButton);
        hitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((GameActivity)getActivity()).hit();
            }
        });

        Button standButton = (Button) view.findViewById(R.id.standButton);
        standButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((GameActivity)getActivity()).stand();
            }
        });

        Button playAgainButton = (Button) view.findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((GameActivity)getActivity()).newGame();
            }
        });

        Button doubleDownButton = (Button) view.findViewById(R.id.doubleButton);
        doubleDownButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((GameActivity)getActivity()).doubleDown();
            }
        });

        Button splitButton = (Button) view.findViewById(R.id.splitButton);
        splitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((GameActivity)getActivity()).split();
            }
        });

        return view;
    }
}
