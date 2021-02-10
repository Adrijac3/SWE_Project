package Views;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Main.Bowler;
import Main.Lane;
import Main.LaneEvent;
import Main.LaneObserver;
import Main.Pinsetter;
import Main.PinsetterEvent;
import Main.PinsetterObserver;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {
    int laneNum;
    boolean laneShowing;
    boolean psShowing;
    private final JPanel jp;
    private final JLabel curBowler;
    private final JLabel pinsDown;
    private final JButton viewLane;
    private final JButton viewPinSetter;
    private final JButton maintenance;
    private final PinSetterView psv;
    private final LaneView lv;
    private final Lane lane;

    public LaneStatusView(Lane lane, int laneNum) {
        this.lane = lane;
        this.laneNum = laneNum;

        laneShowing = false;
        psShowing = false;

        psv = new PinSetterView(laneNum);
        Pinsetter ps = lane.getPinsetter();
        ps.subscribe(psv);

        lv = new LaneView(lane, laneNum);
        lane.subscribe(lv);

        jp = new JPanel();
        jp.setLayout(new FlowLayout());
        JLabel cLabel = new JLabel("Now Bowling: ");
        curBowler = new JLabel("(no one)");
        new JLabel("Foul: ");
        new JLabel(" ");
        JLabel pdLabel = new JLabel("Pins Down: ");
        pinsDown = new JLabel("0");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        new Insets(4, 4, 4, 4);

        viewLane = new JButton("View Lane");
        JPanel viewLanePanel = new JPanel();
        viewLanePanel.setLayout(new FlowLayout());
        viewLane.addActionListener(this);
        viewLanePanel.add(viewLane);

        viewPinSetter = new JButton("Pinsetter");
        JPanel viewPinSetterPanel = new JPanel();
        viewPinSetterPanel.setLayout(new FlowLayout());
        viewPinSetter.addActionListener(this);
        viewPinSetterPanel.add(viewPinSetter);

        maintenance = new JButton("     ");
        maintenance.setBackground(Color.GREEN);
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new FlowLayout());
        maintenance.addActionListener(this);
        maintenancePanel.add(maintenance);

        viewLane.setEnabled(false);
        viewPinSetter.setEnabled(false);

        buttonPanel.add(viewLanePanel);
        buttonPanel.add(viewPinSetterPanel);
        buttonPanel.add(maintenancePanel);

        jp.add(cLabel);
        jp.add(curBowler);
//		jp.add( fLabel );
//		jp.add( foul );
        jp.add(pdLabel);
        jp.add(pinsDown);

        jp.add(buttonPanel);
    }

    public JPanel showLane() {
        return jp;
    }

    public void actionPerformed(ActionEvent e) {
        if (lane.isPartyAssigned() && e.getSource().equals(viewPinSetter)) {
		    if (!psShowing) {
		        psv.show();
		        psShowing = true;
		    } else {
		        psv.hide();
		        psShowing = false;
		    }
		}
        if (e.getSource().equals(viewLane) && lane.isPartyAssigned()) {
		    if (!laneShowing) {
		        lv.show();
		        laneShowing = true;
		    } else {
		        lv.hide();
		        laneShowing = false;
		    }
		}
        if (e.getSource().equals(maintenance) && lane.isPartyAssigned()) {
		    lane.unPauseGame();
		    maintenance.setBackground(Color.GREEN);
		}
    }

    public void receiveLaneEvent(LaneEvent le) {
        curBowler.setText(le.getBowler().getNickName());
        if (le.isMechanicalProblem()) {
            maintenance.setBackground(Color.RED);
        }
        if (!lane.isPartyAssigned()) {
            viewLane.setEnabled(false);
            viewPinSetter.setEnabled(false);
        } else {
            viewLane.setEnabled(true);
            viewPinSetter.setEnabled(true);
        }
    }

    public void receivePinsetterEvent(PinsetterEvent pe) {
        pinsDown.setText(Integer.valueOf(pe.totalPinsDown()).toString());
//		foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );
    }
}
