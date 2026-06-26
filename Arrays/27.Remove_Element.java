class Solution {
    public int removeElement(int[] nums, int val) {
        // int i=0;
        // for(int j=0;j<nums.length;j++){
        //     if(nums[j] != val){
        //         int temp = nums[i];
        //         nums[i] = nums[j];
        //         nums[j] = temp;
        //         i++;
        //     }
        // }
        // return i;


        List<Integer>al = new ArrayList<Integer>();
        for(int i=0;i<nums.length;i++){
            if(nums[i] != val){
                al.add(nums[i]);
            }
        }
        int cnt=0;
        for(int i=0;i<al.size();i++){
            nums[cnt] = al.get(i);
            cnt++;
        }
        return cnt;
    }
}