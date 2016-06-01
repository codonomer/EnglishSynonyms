        //Codonomer.com
		
		@Override
        protected ObservableList<String> call() throws Exception {
            // artificially pause for a while to simulate a long running database connection.
            //Thread.sleep(1000);


            ObservableList<String> words = FXCollections.observableArrayList();

            String sql_word = "SELECT word_id,word FROM pat_Words order by word_id";
            //String sql_word = "SELECT top 19 pat_Words.word_id,pat_Words.word,pat_Synonyms.synonym FROM pat_Words inner join pat_Synonyms on pat_Words.word_id = pat_Synonyms.word_id  ";
            String sql_synonym = "Select synonym from pat_Synonyms where word_id = ? order by id";
            //List<String> list = new ArrayList<>();


            int total_words = 0, total_syn = 0;


            Connection connection = null;
            try {
                try {
                    connection = getConnection();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                PreparedStatement statement = connection.prepareStatement(sql_word);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    //---
                    total_words++;
                    //System.out.println(resultSet.getString(1) + "," + resultSet.getString(2));


                    //list.clear();
                    statement = connection.prepareStatement(sql_synonym);
                    statement.setString(1, resultSet.getString(1));
                    ResultSet resultSet_syn = statement.executeQuery();

                    String line = "";
                    line = resultSet.getString(1) + " - " + resultSet.getString(2) + " : ";


                    int count = 0;

                    while (resultSet_syn.next()) {

                        //list.add(resultSet.getString(0));
                        line += resultSet_syn.getString(1) + " , ";
                        //words.add(line); // Add to List
                        count++;

                    }

                    updateMessage(total_words + " , " + line);
                    //Thread.sleep(1000);


                    //Has Result
                    if (count > 0) {
                        WriteToFileExample.Append(line);
                    }


                    //---
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnection(connection);
                System.out.println("Total Word processed : " + total_words);
                return words;


            }


        }
