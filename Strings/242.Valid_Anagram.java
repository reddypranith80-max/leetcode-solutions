class Solution {
    public boolean isAnagram(String s, String t) {
        int sval = s.length();
        int tval = t.length();

        if(sval != tval)return false;
        int mapping[] = new int[26];
        
        for(int i=0;i<sval;i++){
            int check = s.charAt(i);
            mapping[s.charAt(i)-97]++;
        }
        for(int i=0;i<tval;i++){
            int revcheck = t.charAt(i);
            mapping[t.charAt(i)-97]--;
        }

        for(int i=0;i<26;i++){
            if(mapping[i] != 0)return false;
        }
        return true;
    }
}