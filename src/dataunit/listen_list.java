package dataunit;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class listen_list {
    HashMap<Integer, String> word_list;
    String filename;

    public listen_list(String s) {
        word_list = new HashMap<>();
        this.filename = s;
        File file = new File(s);
        if (file.exists()) {
            read_all_word(filename);
        }
    }

    //添加新的单词
    public void add_new_word(String s) {
        word_list.put(getsize(), s);
    }

    //获取最后一个index
    public int getsize() {
        return word_list.size();
    }

    //修改单词
    public void update_word(int i, String s) {
        word_list.put(i, s);
    }

    //写入所有单词
    public void write_all_word(String list) {
        Set<Map.Entry<Integer, String>> entries = word_list.entrySet();
        try (OutputStreamWriter output_t = new OutputStreamWriter(new FileOutputStream(list, false));
             BufferedWriter output = new BufferedWriter(output_t);) {
            for (Map.Entry i : entries) {
                String word = (String) i.getValue();
                Integer num = (Integer) i.getKey();
                output.write(num.toString() + "-" + word + ",");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取所有单词
    public void read_all_word(String list) {

        String line;
        try (InputStreamReader input_t = new InputStreamReader(new FileInputStream(list), "UTF-8");
             BufferedReader input = new BufferedReader(input_t);) {
            String s = "";
            while ((line = input.readLine()) != null) {
                String s1 = line.trim();
                s += s1;
            }
            System.out.println(s);
            String[] slist = s.split(",");

            for (String i : slist) {
                String[] word_l = i.split("-");
                if (word_l.length == 2) {
                    word_list.put(new Integer(word_l[0].trim()), word_l[1].trim());
                } else if (word_l.length == 1) {//空字符串
                    word_list.put(new Integer(word_l[0].trim()), " ");
                } else if (word_l.length > 2) {
                    //判断是否是带有-的连词
                    String word_ = "";
                    for (int k = 1; k < word_l.length; k++) {
                        if (k != word_l.length-1) {
                            word_ = word_ + word_l[k] + "-";
                        } else {
                            word_ = word_ + word_l[k];
                        }
                    }
                    word_list.put(new Integer(word_l[0].trim()), word_);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //读取记录
    public String read_review(String list) {
        String line;
        try (InputStreamReader input_t = new InputStreamReader(new FileInputStream(list), "UTF-8");
             BufferedReader input = new BufferedReader(input_t);) {
            String s = "";
            while ((line = input.readLine()) != null) {
                String s1 = line.trim();
                s += s1 + "\n";
            }
            return s;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //读取所有单词
    //写入所有单词
    public void write_review(String list, String content, Boolean b) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        String date_string = sdf.format(date);
        try (OutputStreamWriter output_t = new OutputStreamWriter(new FileOutputStream(list, b));
             BufferedWriter output = new BufferedWriter(output_t);) {
            /*如果是false说明是temporary的文件，需要添加时间*/
            if (!b) {
                output.write(date_string + "\n");
            }
            output.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String get_word(int i) {
        return word_list.get(i);
    }



    //移除最后一个单词
    public void remove_word(){
        word_list.remove(getsize()-1);
    }


    //变成纵向测试的列表

}
