
package juice;
import java.io.*;
import java.util.*;

public class Juice {

    private ArrayList<String> components;

    public void setComponents(ArrayList<String> components) {
        this.components = components;
    }

    public Juice juiceCreation() throws IOException {
        Juice juice = new Juice();
        ArrayList<String> list = new ArrayList<String> ();
        FileWorker.readFromFile("juice.in", list);
        juice.setComponents(list);
        return juice;
    }

    @Override
    public String toString() {
        return (this.components.toString());
    }

    public LinkedHashSet<String> getListOfComponents() {
        LinkedHashSet<String> set = new LinkedHashSet<String> ();
        for(int i = 0; i < this.components.size(); i++) {
            StringTokenizer st = new StringTokenizer(this.components.get(i), " ");
            while (st.hasMoreTokens())
                set.add(st.nextToken());
        }
        return set;
    }

    public void listMentioned() throws FileNotFoundException {
        if (!(this.components.isEmpty())) {
            LinkedHashSet<String> set = this.getListOfComponents();
            FileWorker.writeInFile("juice1.out", set);
        }
        else {
            try {
                FileWorker.write("juice3.out", "No elements.");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void sortListOfComponents() throws FileNotFoundException {
        if (!(this.components.isEmpty()))  {
            LinkedHashSet<String> set = this.getListOfComponents();
            ArrayList<String> list = new ArrayList<String>(set);
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            FileWorker.writeInFile("juice2.out", new LinkedHashSet<String>(list));
        }
            else {
            try {
                FileWorker.write("juice3.out", "No elements.");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public boolean isEntry(ArrayList<String> content,ArrayList<String> store) {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < store.size(); j++)
            sb.append(store.get(j));
        for (int i = 0; i < content.size(); i++) {
            String check = content.get(i);
            if(sb.indexOf(check) == -1)
                return false;
        }
       return true;
    }

    public ArrayList<ArrayList<String>> swap (ArrayList<ArrayList<String>> list, int firstIndex,int secondIndex) {
        ArrayList<ArrayList<String>> newList = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < list.size(); i++) {
            if (i == firstIndex) {
                newList.add(i, list.get(secondIndex));
            }
            else
            if (i == secondIndex)
                newList.add(i, list.get(firstIndex));
            else
                newList.add(i, list.get(i));
        }
        return newList;
    }

    public void minimalNumberOfWashing() {
        if (!(this.components.isEmpty()))  {
            LinkedHashSet<ArrayList<String>> newNames = new LinkedHashSet<ArrayList<String>>();
            for(int i = 0;i < this.components.size();i++) {
                ArrayList<String> list = new ArrayList<String> ();
                StringTokenizer st = new StringTokenizer(this.components.get(i), " ");
                while (st.hasMoreTokens()) {
                    list.add(st.nextToken());
                }
                Collections.sort(list, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                newNames.add(list);
            }
            ArrayList<ArrayList<String>> name = new ArrayList<ArrayList<String>> ();
            for(ArrayList<String> a : newNames) {
                name.add(a);
            }
            for(int i = name.size() - 1;i >= 0; i--)
                for (int j = 0; j < i; j++)
                    if (name.get(j).size() > name.get(j + 1).size()) {
                        name = swap(name, j, j + 1);
                    }
            int count;
            for(int i = 0;i < name.size() - 1;i++) {
                count = i;
                for (int j = count + 1; j < name.size(); j++) {
                    if (isEntry(name.get(i), name.get(j)))
                        name = swap(name, ++count, j);
                }
                i = count + 1;
            }
            count = 0;
            for(int i = 0;i < name.size() - 1;i++)
                if(!(isEntry(name.get(i),name.get(i + 1))))
                    count++;
            count++;
                FileWorker.write("juice3.out", Integer.toString(count));
        }
        else {
                FileWorker.write("juice3.out", "No elements.");
        }
    }
}