/*
	Abyte0
	2012-02-16
	Version 1.3 - 2012-05-04
	Version 1.4 - 2021-06-22 Update to OpenApos
	Version 1.5 - 2021-12-01 Talk + DateTime
	Version 1.5.1 - 2021-12-29 Call the base class then override the general chat
	Version 1.6 - 2022-01-08 Script version
*/
import java.net.*; 
import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import com.aposbot.*;
import com.aposbot._default.*;

public class Abyte0_Script extends Storm_Script 
{
    public Extension client;
	
	boolean waitingBeforeLastDrop = false;
	
	public int[] ALL_KNOWN_FOODS = new int[] 
	{
		335, //cake 1/3
		333, //cake 2/3
		330, //cake 3/3
		332, // Chocolate Cake
		334, // Partial Chocolate Cake
		336, // Chocolate Slice
		895, //Swamp Toad		
		897, //King worm		
		138, //bread		
		142, //wine
		373, //Lobs
		357, //Salmon
		325, // Plain Pizza
		326, // Meat Pizza
		327, // Anchovie Pizza
		328, // Half Meat Pizza
		329, // Half Anchovie Pizza
		346, // Stew
		355, // Sardine
		357, // Salmon
		359, // Trout
		362, // Herring
		364, // Pike
		367, //Tunas
		373, //Lobs
		370, //Swordfish
		551, //Cod
		553, //Mackerel
		555, //Bass
		1191, //Manta ray
		1193 //Sea turtle
	};		
		
	int oakTree = 306;
	int oakLog = 632;
	int oakLongBow = 648;
	int oakLongBowU = 658;
	
	int willowTree = 307;
	int willowLog = 633;
	int willowLongBow = 650;
	int willowLongBowU = 660;
	
	int yewTree = 309;
	int yewLog = 635;
	int yewLongBow = 654;
	int yewLongBowU = 664;
	
	int magicTree = 310;
	int magicLog = 636;
	int magicLongBow = 656;
	int magicLongBowU = 666;
	
	int bowString = 676;
	
	public static String[] PROPERTY_NAMES = new String[]{"nom", 
		"money",
	    "feathers",
	    "chaosRunes",
	    "natureRunes",
	    "ironOres",
	    "coalOres",
	    "mithOres",
	    "addyOres",
	    "runiteOres",
	    "ironBars",
		"steelBars",
		"runiteBars",
		"bowStrings",
		"yewLongU",
		"yewLong",
		"magicLongU",
		"magicLong",
		"rawLobs",
		"cookLobs",
		"rawSharks",
		"cookSharks"};
	
	public static String[] PROPERTY_NAMES_STATS = new String[]{"nom" 
		,"attack"
		,"defence"
		,"strength"
		,"hits"
		,"prayer"
		,"magic"
		,"ranged"
		,"fishing"
		,"cooking"
		,"mining"
		,"smithing"
		,"woodcut"
		,"fletching"
		,"agility"
		,"firemaking"
		,"crafting"
		,"herblaw"
		,"thieving"};
	
	
	public Abyte0_Script(Extension e) {
		super(e);
        this.client = e;
		}    
	
	public void useSleepingBag()
	{
		//sendPosition(AutoLogin.user,getX(),getY());
		printInventory();
		printStats();
		printStatsXp();
		//printBot("@mag@Thieving Xp: @or3@" + getExperience(17));
		super.useSleepingBag();
	}

	public void buyItemIdFromShop(int id, int amount)
	{
		int position = getShopItemById(id);
		if(position == -1) return;
		
		buyShopItem(position, amount);
	}
	public int getShopItemIdAmount(int id)
	{
		int position = getShopItemById(id);
		
		return getShopItemAmount(position);
	}
	
	public void print(String gameText)
	{
		System.out.println(gameText);
		printBot(gameText);
	}
	
	//* BUILDS METHODS *//
	
	public String[] getRandomQuotes()
	{
		String[] result = {""};
		return result;
	}
	
	public int delayDice = 10000;
	
	protected boolean switchUserForNextRelog(String name) {
        
		if (name == null) {
            System.out.println("You didn't enter an account to use with autologin.");
            System.out.println("You can still use APOS, but it won't be able to log you back in if you disconnect.");
            return false;
        }
        final Properties p = new Properties();
        try (FileInputStream stream = new FileInputStream("." + File.separator + "Accounts" + File.separator + name + ".properties")) {
            p.load(stream);

			AutoLogin login = (AutoLogin)client.getAutoLogin();
			login.setAccount(p.getProperty("username"), p.getProperty("password"));		
			
        } catch (final Throwable t) {
            return false;
        }
		
		return true;
    }
	
    public IScript initJavaScript(String name) {
        ScriptFrame scriptLoader = new ScriptFrame(client);
		return scriptLoader.initJavaScript(name);
    }

	protected boolean switchUserForNextRelog(String name, String password) {
        
			AutoLogin login = (AutoLogin)client.getAutoLogin();
			login.setAccount(name, password);
			
		return true;
    }
	
	public void SayRandomQuote()
	{
		String[] results = getRandomQuotes();
		
		if(random(0,delayDice) != 1)
			return;
		
		int selectedQuote = random(1,results.length) -1;
		
		if("".equals(results[selectedQuote]))
			return;
		
		Say(results[selectedQuote]);
	}
	
	public void Say(String content)
	{
		setTypeLine(content);
		while(!next());
	}
	
    @Override
    public void onChatMessage(String msg, String name, boolean pmod, boolean jmod) {
		
		//Do not reply to yourself
		final String lname = client.getPlayerName(client.getPlayer());		
        if(name.equalsIgnoreCase(lname)) return;
		
		String receivedLC = msg.toLowerCase();
		
        if ((receivedLC.contains("level") || receivedLC.contains("lvl")) && receivedLC.contains("?")) {
			if (receivedLC.contains("cook"))
				Say("I am " + getLevel(7));
			if (receivedLC.contains("wood") || receivedLC.contains("wc"))
				Say("I am " + getLevel(8));
			if (receivedLC.contains("fletch"))
				Say("I am " + getLevel(9));
			if (receivedLC.contains("fish"))
				Say("I am " + getLevel(10));
			if (receivedLC.contains("fire"))
				Say("I am " + getLevel(11));
			if (receivedLC.contains("craft"))
				Say("I am " + getLevel(12));
			if (receivedLC.contains("smith"))
				Say("I am " + getLevel(13));
			if (receivedLC.contains("mining")|| receivedLC.contains("mine"))
				Say("I am " + getLevel(14));
			if (receivedLC.contains("herb"))
				Say("I am " + getLevel(15));
			if (receivedLC.contains("agility"))
				Say("I am " + getLevel(16));
			if (receivedLC.contains("thieving") || receivedLC.contains("thieve") || receivedLC.contains("thief"))
				Say("I am " + getLevel(17));

			//https://stackoverflow.com/questions/2286648/named-placeholders-in-string-formatting
        }
		
		
		if (receivedLC.equals("version") && !getSctiptVersion().equals(""))
			Say("Script Version " + getSctiptVersion());
		
		
		super.onChatMessage(msg, name, pmod, jmod);
    }

	public void printInventory()
	{
		//String nom = AutoLogin.user;
		int money = 10;
		int feathers = 381;
		int chaosRunes = 41;
		int natureRunes = 40;
		int ironOres = 151;
	    int coalOres = 155;
	    int mithOres = 153;
	    int addyOres = 154;
	    int runiteOres = 409;
	    int ironBars = 170;
		int steelBars = 171;
		int runiteBars = 408;
		int bowStrings = 676;
		int yewLongU = 664;
		int yewLong = 654;
		int magicLongU = 666;
		int magicLong = 656;
		int rawLobs = 372;
		int cookLobs = 373;
		int rawSharks = 545;
		int cookSharks = 546;
		
		int[] ids = new int[]{money,feathers,chaosRunes,natureRunes,
		ironOres,coalOres,mithOres,addyOres,runiteOres,ironBars,steelBars,runiteBars,
		bowStrings,yewLongU,yewLong,magicLongU,magicLong,rawLobs,cookLobs,rawSharks,cookSharks};
	
		String[] valeurs = new String[22];
		//valeurs[0] = nom;
		
		for(int i = 0; i < 21; i++)
		{
			int[] bk = new int[]{ids[i]};
			
			valeurs[i+1] = getInventoryCount(bk)+"";
		}
	
		sendInventory(valeurs);
	}
	
	public void printStats()
	{			
		//String nom = AutoLogin.user;
		
		int attack = getLevel(0);
		int defence = getLevel(1);
		int strength = getLevel(2);
		int hits = getLevel(3);
		int prayer = getLevel(5);
		int magic = getLevel(6);
		int ranged = getLevel(4);
		
		int fishing = getLevel(10);
		int cooking = getLevel(7);
		int mining = getLevel(14);
		int smithing = getLevel(13);
		int woodcut = getLevel(8);
		int fletching = getLevel(9);
		int agility = getLevel(16);
		int firemaking = getLevel(11);
		int crafting = getLevel(12);
		int herblaw = getLevel(15);
		int thieving = getLevel(17);
		
		int[] ids = new int[]{attack, defence, strength, hits, prayer, magic, ranged, fishing, cooking, mining, smithing, woodcut, fletching, agility, firemaking, crafting, herblaw, thieving};
	
		String[] valeurs = new String[19];
		//valeurs[0] = nom;
		
		for(int i = 0; i < 18; i++)
		{
			valeurs[i+1] = ids[i]+"";
		}
	
		sendStats(valeurs);
	}
	
	public void printStatsXp()
	{			
		//String nom = AutoLogin.user;
		
		int attack = getExperience(0);
		int defence = getExperience(1);
		int strength = getExperience(2);
		int hits = getExperience(3);
		int prayer = getExperience(5);
		int magic = getExperience(6);
		int ranged = getExperience(4);
		
		int fishing = getExperience(10);
		int cooking = getExperience(7);
		int mining = getExperience(14);
		int smithing = getExperience(13);
		int woodcut = getExperience(8);
		int fletching = getExperience(9);
		int agility = getExperience(16);
		int firemaking = getExperience(11);
		int crafting = getExperience(12);
		int herblaw = getExperience(15);
		int thieving = getExperience(17);
		
		int[] ids = new int[]{attack, defence, strength, hits, prayer, magic, ranged, fishing, cooking, mining, smithing, woodcut, fletching, agility, firemaking, crafting, herblaw, thieving};
	
		String[] valeurs = new String[19];
		//valeurs[0] = nom;
		
		for(int i = 0; i < 18; i++)
		{
			valeurs[i+1] = ids[i]+"";
		}
	
		sendStatsXp(valeurs);
	}

	//* SEND METHODS *//
	public void sendPosition(String name,int x, int y) 
	{
	} 
	
	public void sendInventory(String[] propertyUsed, String[] values) 
	{
	} 
	
	public void sendStats(String[] propertyUsed, String[] values) 
	{
	} 
	
	public void sendStatsXp(String[] propertyUsed, String[] values) 
	{
	} 
	
	public void sendStats(String[] values)
	{
		sendStats(PROPERTY_NAMES_STATS, values);
	}
	
	public void sendStatsXp(String[] values)
	{
		sendStatsXp(PROPERTY_NAMES_STATS, values);
	}
	
	public void sendInventory(String[] values)
	{
		sendInventory(PROPERTY_NAMES, values);
	}
	
	public void createAccount(String name) 
	{
	}
	
	public int getExperience(int skill) {
        return (int) client.getExperience(skill);
    }	
	
	    /**
     * Returns the position of the item with the given ID in the client's
     * inventory.
     *
     * @param ids the identifiers of the items to search for.
     * @return the position of the first item with the given id(s). May range
     * from 0 to MAX_INV_SIZE.
     */
    public int getLastInventoryIndex(int... ids) {
        for (int i = getInventoryCount()-1; i >=0 ; i--) {
            if (inArray(ids, client.getInventoryId(i))) {
                return i;
            }
        }
        return -1;
    }

	    /**
     * Drop the 
     */
    public int dropItemIdOrWait(int id) {
		
        int firstInstanceIndex = getInventoryIndex(id);
		if(firstInstanceIndex == -1)
			return -1;
		
        int lastInstanceIndex = getLastInventoryIndex(id);
		
		if(!waitingBeforeLastDrop && firstInstanceIndex == lastInstanceIndex) //Lets wait a bit before dropping the last one
		{
			waitingBeforeLastDrop = true;
			return 2000;
		}
		
		dropItem(firstInstanceIndex);
		waitingBeforeLastDrop = false;
		
		return 1500;
    }

	public int[][] getAllNpcsById(int... ids)
	{
		int cpt = 0;
		for (int i = 0; i < client.getNpcCount(); i++) {
			if (inArray(ids, client.getNpcId(client.getNpc(i))))
				cpt++;
		}
		
		int[][] npcS = new int[cpt][];
		
		int cptAdded = 0;

		for (int i = 0; i < client.getNpcCount(); i++) {
			if (inArray(ids, client.getNpcId(client.getNpc(i)))) {
				final int x = client.getMobLocalX(client.getNpc(i)) + client.getAreaX();
				final int y = client.getMobLocalY(client.getNpc(i)) + client.getAreaY();
				final int dist = distanceTo(x, y, getX(), getY());
				if (dist < 10)
				{
					final int[] npc = new int[]{-1, -1, -1};
					
					npc[0] = i;
					npc[1] = x;
					npc[2] = y;
					
					npcS[cptAdded]  = npc;
				}
			}
		}
		return npcS;
	}
	
	public void RunFromCombat()
	{
		walkTo(getX(),getY());
	}
	
	public boolean IsStillHavingFood(int foodId)
	{
		if(foodId == -1) return true;
		if(foodId == 330)
			return getInventoryCount(foodId,333,335) > 0;
		else
			return getInventoryCount(foodId) > 0;
	}
	
	public final void EatFood(int foodId)
	{
		if(foodId == -1) return;
		
		if(foodId == 330)
		{
			EatCake();
		}
		else
		{
			int foodIndex = getInventoryIndex(foodId);
			useItem(foodIndex);
		}
	}
	
	private void EatCake()
	{
		int part1 = getInventoryIndex(335);
		int part2 = getInventoryIndex(333);
		int part3 = getInventoryIndex(330);
		if(part1 != -1)
		{
			useItem(part1);
		}
		else if(part2 != -1)
		{
			useItem(part2);
		}
		else if(part3 != -1)
		{
			useItem(part3);
		}
	}
	
	public String getDateTime()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
	
	public String getSctiptVersion()
	{
		return "";
	}
	
	private int[] raresItems = new int[]{112,526,527,1276,1277,1278,795,575,576,577,578,579,580,581,597};
	
	public int pickupRareItems(int maxDistance)
	{
		for(int h = 0; h < raresItems.length; h++)
		{
			int[] groundItems = getItemById(raresItems[h]);
			if(groundItems[0] != -1)
			{
				if(inCombat())
				{
					RunFromCombat();
					return 500;
				}

				if(isAtApproxCoords(groundItems[1], groundItems[2], maxDistance))
				{
					pickupItem(groundItems[0], groundItems[1], groundItems[2]);
					print("rare item picked up");
					Say("WHAT!");
					return 300;
				}
			}
		}
		
		return -1;
	}
	
}