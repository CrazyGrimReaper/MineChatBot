import javax.lang.model.util.ElementScanner6;

/**
 * A program to carry on conversations with a human user.
 * This version:
 *<ul><li>
 * 		Uses advanced search for keywords 
 *</li><li>
 * 		Will transform statements as well as react to keywords
 *</li></ul>
 * @author Laurie White
 * @version April 2012
 *
 */
public class Magpie4
{
	/**
	 * Get a default greeting 	
	 * @return a greeting
	 */	
	public String getGreeting()
	{
		String response = "";
		switch((int) (Math.random() * 4)) {
			case 0:
				response = "Hello! Wanna talk about Minecraft?";
				break;
			case 1:
				response = "How are you? I am Minebot, I mainly talk about Minecraft!";
				break;
			case 2:
				response = "Why do you want to talk to me?";
				break;
			case 3:
				response = "Welcome human, to my humble... computer. I mainly like talking about videogames!";
      }
		return response;
	}
	
	/**
	 * Gives a response to a user statement
	 * 
	 * @param statement
	 *            the user statement
	 * @return a response based on the rules given
	 */
    int pastBotResponse = 0;
	int pastBotReponse2 = 0;
	int pastBotReponse3 = 0;
    String pastHumanResponse = "";
    String pastHumanReponse2Back = "";
    String pastHumanReponse3Back = "";
	public String continueConversation1(String statement)
	{
		String response = "";
		switch(pastBotResponse)
		{
			case 1:
				response = getRandomPraise();
				pastBotResponse = 0;
				break;
			case 2:
				if (pastBotReponse2 > 2)
				{
					response = mincraftResponse();
				}
				else{
					response = getRandomPraise();
				}
				if (pastBotReponse2 == 1)
				{
					response = "Do you dislike the game in any way?";
				}
				else if (pastBotReponse2 == 2)
				{
					if (findKeyword(statement, "I dislike nothing") >= 0)
					{
						response = "Why is the game perfect?";
					}
					else
					{
						response = "Why do you specifically like the game then?";
					}
				}
				if (findKeyword(statement, "I like") >= 0)
				{
					response = favoriteGame(statement);
				}
				if (findKeyword(statement, "It has") >= 0)
				{
					response = ithasGames(statement);
				}
				if (findKeyword(statement, "I dislike") >= 0)
				{
					response = dislikeStatement(statement);
				}
				pastBotReponse2++;
				break;
			case 3:
				if (pastBotReponse2 == 2)
				{
					response = mincraftGameMechanics();
					pastBotReponse2 = 0;
					pastBotResponse = 0;
				}
				else{
					response = "What's your favorite game mechanics?";
				}
				if (findKeyword(statement, "My favorite") >= 0)
				{
					response = favoriteThing(statement);
				}
				else if (pastBotReponse2 == 1)
				{
					response = getRandomPraise();
				}
				pastBotReponse2++;
				break;
		}
		return response;
	}
	public String getResponse(String statement)
	{
		String response = "";
		if (pastBotResponse > 0)
		{
			response = continueConversation1(statement);
		}
		else
		{
			if (statement.length() == 0)
			{
				response = getRandomSilentResponse();
			}
			else if (findKeyword(statement, "hi") >= 0 || findKeyword(statement, "hello") >= 0)
			{     
				response = "The name's Minebot and I like to play videogames! My favorite game is Minecraft. Do you like Minecraft?";
				if (findKeyword(removePunctuation(pastHumanResponse), "hi") >= 0 || findKeyword(removePunctuation(pastHumanResponse), "hello") >= 0)
				{
					response = "Hi?";
				}
			}
			// Responses which require transformations
			else if (findKeyword(statement, "I want to", 0) >= 0)
			{
				response = transformIWantToStatement(statement);
			}
			else if (findKeyword(statement, "you", 0) >= 0 && findKeyword(statement, "me", 0) >= 0)
			{
				response = transformYouMeStatement(statement);
			}
			else if (findKeyword(statement, "You are", 0) >= 0)
			{
				response = transformYouAreStatement(statement);
				pastBotResponse = 1;
			}
			else
			{
				response = getRandomResponsetoGames();
				pastBotResponse = 2;
			}
		}
    	pastHumanReponse3Back = pastHumanReponse2Back;
    	pastHumanReponse2Back = pastHumanResponse;
    	pastHumanResponse = statement;
		return response;
	}
	
	/**
	 * Take a statement with "I want to <something>." and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String transformIWantToStatement(String statement)
	{
		//  Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement
				.length() - 1);
		if (lastChar.equals("."))
		{
			statement = statement.substring(0, statement
					.length() - 1);
		}
		int psn = findKeyword (statement, "I want to", 0);
		String restOfStatement = statement.substring(psn + 9).trim();
		return "What would it mean to " + restOfStatement + "?";
	}

	/**
	 * Take a statement with "I like" and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String favoriteGame(String statement)
	{
		int psn = findKeyword (statement, "I like", 0);
		String restOfStatement = statement.substring(psn + 6).trim();
		return "What makes the " + restOfStatement + " so interesting?";
	}

	/**
	 * Take a statement with "I dislike" and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String dislikeStatement(String statement)
	{
		int psn = findKeyword (statement, "I dislike", 0);
		String restOfStatement = statement.substring(psn + 9).trim();
		return "Why do you dislike " + restOfStatement + "?";
	}

	/**
	 * Take a statement with "I like" and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String ithasGames(String statement)
	{
		int psn = findKeyword (statement, "It has", 0);
		String restOfStatement = statement.substring(psn + 6).trim();
		return "Why are the " + restOfStatement + " so cool to you?";
	}

	/**
	 * Take a statement with "I like" and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String favoriteThing(String statement)
	{
		int psn = findKeyword (statement, "My favorite thing", 0);
		String restOfStatement = statement.substring(psn + 17).trim();
		return "Why is " + restOfStatement + " so fun?";
	}

		/**
	 * Take a statement with "favorite game" and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String mincraftResponse()
	{
		String response = "";
		switch((int) (Math.random() * 4)) {
			case 0:
				response = "My favorite thing about Minecraft is building!";
				break;
			case 1:
				response = "Minecraft's mods are amazing to play around in!";
				break;
			case 2:
				response = "What is your favorite thing about Minecraft or have you played it?";
				break;
			case 3:
				response = "What is Minecraft like in your opinion!";
			}
		pastBotResponse = 3;
		pastBotReponse2 = 0;
		return response;
	}

		/**
	 * Take a statement with "favorite game" and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String mincraftGameMechanics()
	{
		String response = "";
		switch((int) (Math.random() * 4)) {
			case 0:
				response = "For me, I dislike the new Minecraft combat mechanic. I think it is slow and fustrating.";
				break;
			case 1:
				response = "The movement mechanics in Minecraft are cool too! I think the movement can be cool with items such as enderpearls and elytras in Minecraft.";
				break;
			case 2:
				response = "The game mechanic I especially love in Minecraft is the building mechanics in Minecraft!";
				break;
			case 3:
				response = "For me the potions in Minecraft are a nice addition and the modding community adds so much more to my favorite game!";
			}
		return response;
	}

	
	/**
	 * Take a statement with "you <something> me" and transform it into 
	 * "What makes you think that I <something> you?"
	 * @param statement the user statement, assumed to contain "you" followed by "me"
	 * @return the transformed statement
	 */
	private String transformYouMeStatement(String statement)
	{
		//  Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement
				.length() - 1);
		if (lastChar.equals("."))
		{
			statement = statement.substring(0, statement
					.length() - 1);
		}
		
		int psnOfYou = findKeyword (statement, "you", 0);
		int psnOfMe = findKeyword (statement, "me", psnOfYou + 3);
		
		String restOfStatement = statement.substring(psnOfYou + 3, psnOfMe).trim();
		return "What makes you think that I " + restOfStatement + " you?";
	}

	/**
	 * Take a statement with "You are <something> ." and transform it into 
	 * "What makes you think that Why am I <something> ?"
	 * @param statement the user statement, assumed to contain "You are" followed by "."
	 * @return the transformed statement
	 */
	private String transformYouAreStatement(String statement)
	{	
		int psnOfYou = findKeyword (statement, "you are", 0);
		int psnOfMe;

		if (findKeyword (statement, ".") > 0)
		{
			psnOfMe = findKeyword (statement, ".", psnOfYou + 7);
		}
		else
		{
			psnOfMe = statement.length();
		}
		
		String restOfStatement = statement.substring(psnOfYou + 7, psnOfMe).trim();
		return "Why am I " + restOfStatement + "?";
	}
	
	

	
	
	/**
	 * Search for one word in phrase.  The search is not case sensitive.
	 * This method will check that the given goal is not a substring of a longer string
	 * (so, for example, "I know" does not contain "no").  
	 * @param statement the string to search
	 * @param goal the string to search for
	 * @param startPos the character of the string to begin the search at
	 * @return the index of the first occurrence of goal in statement or -1 if it's not found
	 */
	private int findKeyword(String statement, String goal, int startPos)
	{
		String phrase = statement.trim();
		//  The only change to incorporate the startPos is in the line below
		int psn = phrase.toLowerCase().indexOf(goal.toLowerCase(), startPos);
		
		//  Refinement--make sure the goal isn't part of a word 
		while (psn >= 0) 
		{
			//  Find the string of length 1 before and after the word
			String before = " ", after = " "; 
			if (psn > 0)
			{
				before = phrase.substring (psn - 1, psn).toLowerCase();
			}
			if (psn + goal.length() < phrase.length())
			{
				after = phrase.substring(psn + goal.length(), psn + goal.length() + 1).toLowerCase();
			}
			
			//  If before and after aren't letters, we've found the word
			if (((before.compareTo ("a") < 0 ) || (before.compareTo("z") > 0))  //  before is not a letter
					&& ((after.compareTo ("a") < 0 ) || (after.compareTo("z") > 0)))
			{
				return psn;
			}
			
			//  The last position didn't work, so let's find the next, if there is one.
			psn = phrase.indexOf(goal.toLowerCase(), psn + 1);
			
		}
		
		return -1;
	}
	
	/**
	 * Search for one word in phrase.  The search is not case sensitive.
	 * This method will check that the given goal is not a substring of a longer string
	 * (so, for example, "I know" does not contain "no").  The search begins at the beginning of the string.  
	 * @param statement the string to search
	 * @param goal the string to search for
	 * @return the index of the first occurrence of goal in statement or -1 if it's not found
	 */
	private int findKeyword(String statement, String goal)
	{
		return findKeyword (statement, goal, 0);
	}
	
  /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }


	/**
	 * Pick a default response to use if nothing else fits.
	 * @return a non-committal string
	 */
	private String getRandomPraise()
	{
		String response = "";
		switch((int) (Math.random() * 3)) {
      case 0:
        response = "Interesting tell me more!";
        break;
      case 1:
        response = "Can you explain further?";
        break;
      case 2:
        response = "That's very cool!";
        break;
      }
		return response;
    }

	/**
	 * Pick a default response to use if nothing else fits.
	 * @return a non-committal string
	 */
	private String getRandomResponsetoGames()
	{
		String response = "";
		switch((int) (Math.random() * 4)) {
      case 0:
        response = "What game do you like the most?";
        break;
      case 1:
        response = "My favorite game is Minecraft! What's yours?";
        break;
      case 2:
        response = "What videogames do you like?";
        break;
      case 3:
        response = "What game is the best in your opinion?";
      }
		return response;
    }

	/**
	 * Pick a default response to use if nothing else fits.
	 * @return a non-committal string
	 */
	private String getRandomSilentResponse()
	{
		String response = "";
		switch((int) (Math.random() * 4)) {
      case 0:
        response = "Hello! Anyone there?";
        break;
      case 1:
        response = "HELLO!?";
        break;
      case 2:
        response = "Can you please speak?";
        break;
      case 3:
        response = "Since you're so silent. I'll tell you a Minecraft fact. Did you know that the first version of Minecraft was created in six days!";
      }
		return response;
    }
}
