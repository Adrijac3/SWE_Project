package Views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


import Main.Bowler;
import Main.BowlerFile;



/** Class for GUI components need to add a party. */

public class AddPartyView implements ActionListener, ListSelectionListener {
    private final int maxSize;

    private final JFrame win;
    private final JButton addPatron;
    private final JButton newPatron;
    private final JButton remPatron;
    private final JButton finished;
    private final JList<Vector> partyList;
    private final JList<Vector> allBowlers;
    private final Vector party;
    private Vector bowlerdb;
    private Integer lock;

    private final ControlDeskView controlDesk;

    private String selectedNick;

	private String selectedMember;

    public AddPartyView(ControlDeskView controlDesk, int max) {
        this.controlDesk = controlDesk;
        maxSize = max;

        win = new JFrame("Add Party");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Party Panel
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Your Party"));

        party = new Vector();
        Vector empty = new Vector();
        empty.add("(Empty)");

        partyList = new JList(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(5);
        partyList.addListSelectionListener(this);
        JScrollPane partyPane = new JScrollPane(partyList);
        //        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            bowlerdb = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector();
        }
        allBowlers = new JList(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

//        Insets buttonMargin = new Insets(4, 4, 4, 4);

        addPatron = new JButton("Add to Party");
        JPanel addPatronPanel = new JPanel();
        addPatronPanel.setLayout(new FlowLayout());
        addPatron.addActionListener(this);
        addPatronPanel.add(addPatron);

        remPatron = new JButton("Remove Member");
        JPanel remPatronPanel = new JPanel();
        remPatronPanel.setLayout(new FlowLayout());
        remPatron.addActionListener(this);
        remPatronPanel.add(remPatron);

        newPatron = new JButton("New Patron");
        JPanel newPatronPanel = new JPanel();
        newPatronPanel.setLayout(new FlowLayout());
        newPatron.addActionListener(this);
        newPatronPanel.add(newPatron);

        finished = new JButton("Finished");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);

        buttonPanel.add(addPatronPanel);
        buttonPanel.add(remPatronPanel);
        buttonPanel.add(newPatronPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(
                screenSize.width / 2 - win.getSize().width / 2,
                screenSize.height / 2 - win.getSize().height / 2);
        win.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addPatron) && selectedNick != null && party.size() < maxSize) {
		    if (party.contains(selectedNick)) {
		        System.err.println("Member already in Party");
		    } else {
		        party.add(selectedNick);
		        partyList.setListData(party);
		    }
		}
        if (e.getSource().equals(remPatron) && selectedMember != null) {
		    party.removeElement(selectedMember);
		    partyList.setListData(party);
		}
        if (e.getSource().equals(newPatron)) {
            NewPatronView newPatron = new NewPatronView(this);
        }
        if (e.getSource().equals(finished)) {
            if (party != null && !party.isEmpty()) {
                controlDesk.updateAddParty(this);
            }
            win.setVisible(false);
        }
    }

    /**
     * Handler for List actions.
     * @param e the ListActionEvent that triggered the handler
     */

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            selectedNick =
                    (String) ((JList) e.getSource()).getSelectedValue();
        }
        if (e.getSource().equals(partyList)) {
            selectedMember =
                    (String) ((JList) e.getSource()).getSelectedValue();
        }
    }

    /** Accessor for Party. */

    public Vector getNames() {
        return party;
    }

    /**
     * Called by NewPatronView to notify AddPartyView to update.
     *
     * @param newPatron the NewPatronView that called this method
     */

    public void updateNewPatron(NewPatronView newPatron) {
        try {
            Bowler checkBowler = BowlerFile.getBowlerInfo(newPatron.getNick());
            if (checkBowler == null) {
                BowlerFile.putBowlerInfo(
                        newPatron.getNick(),
                        newPatron.getFull(),
                        newPatron.getEmail());
                bowlerdb = new Vector(BowlerFile.getBowlers());
                allBowlers.setListData(bowlerdb);
                party.add(newPatron.getNick());
                partyList.setListData(party);
            } else {
                System.err.println("A Bowler with that name already exists.");
            }
        } catch (Exception e2) {
            System.err.println("File I/O Error");
        }
    }

    /** Accessor for Party. */

    public Vector getParty() {
        return party;
    }
}
