class Solution {
    public boolean containsDuplicate(int[] nums) {
        Arrays.sort(nums);
            boolean isDup = false;
        for(int i=0;i<nums.length-1;i++){
            if(nums[i] == nums[i+1]){
                return true;
            }
        }
        return false;
    }
}

// hashset solution

class Solution {
    public boolean containsDuplicate(int[] nums) {
       int n = nums.length;
        HashSet<Integer>hs = new HashSet<>();

        for(int i=0;i<n;i++){
            if(hs.contains(nums[i])){
                return true;
            }
            hs.add(nums[i]);
        }
        return false;
    }
}