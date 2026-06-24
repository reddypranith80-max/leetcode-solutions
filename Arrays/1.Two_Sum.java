class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        int val[] = new int[2];
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                if(nums[i] + nums[j] == target){
                    val[0] = i;
                    val[1] = j;
                }
            }
        }
        return val;
    }
}

// hashmap solution

class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        int val[] = new int[2];
        HashMap<Integer,Integer>hm = new HashMap<>();
            for(int i=0;i<n;i++){
                int comp = target - nums[i];
                if(hm.containsKey(comp)){
                    return new int[]{hm.get(comp),i};
                }
                hm.put(nums[i],i);
            }
            return new int[]{};
    }
}

